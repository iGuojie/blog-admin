package com.yu.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yu.blog.bean.PageContent;
import com.yu.blog.bean.Pagination;
import com.yu.blog.dao.ImageMapper;
import com.yu.blog.dto.ImageDto;
import com.yu.blog.entity.Image;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.HexFormat;

@Service
public class ImageService {

    private static final List<Integer> VARIANT_PERCENTAGES = List.of(30, 50, 70);
    private static final DateTimeFormatter YEAR_MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");

    private final ImageMapper imageMapper;
    private final Tika tika = new Tika();

    @Value("${image.storage.root:./data/image-store}")
    private String storageRoot;

    @Value("${image.storage.public-base-url:}")
    private String publicBaseUrl;

    public ImageService(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    public ImageDto upload(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "????????");
        }

        byte[] bytes;
        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "????????", e);
        }

        if (bytes.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "??????");
        }

        String sha256 = computeSha256(bytes);
        Image existing = imageMapper.selectOne(new LambdaQueryWrapper<Image>().eq(Image::getSha256, sha256));
        if (existing != null) {
            if (Boolean.TRUE.equals(existing.getDeleted())) {
                existing.setDeleted(false);
                imageMapper.updateById(existing);
            }
            return toDto(existing);
        }

        String originalName = multipartFile.getOriginalFilename();
        String detectedMime = tika.detect(bytes, originalName);
        if (detectedMime == null || !detectedMime.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "?????????");
        }

        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "????????", e);
        }

        if (bufferedImage == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "????????");
        }

        String ext = resolveExtension(originalName, detectedMime);
        String baseKey = buildBaseKey(sha256);
        Path baseDir = Paths.get(storageRoot).resolve(baseKey);
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "??????????", e);
        }

        Path originalPath = baseDir.resolve("original." + ext);
        try {
            Files.write(originalPath, bytes);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "??????", e);
        }

        generateVariants(bufferedImage, ext, baseDir);

        Image image = new Image();
        image.setOriginalName(originalName);
        image.setExt(ext);
        image.setMimeType(detectedMime);
        image.setSizeBytes((long) bytes.length);
        image.setSha256(sha256);
        image.setBaseKey(baseKey.replace('\\', '/'));
        image.setWidth(bufferedImage.getWidth());
        image.setHeight(bufferedImage.getHeight());
        image.setDeleted(Boolean.FALSE);
        image.setCreatedAt(LocalDateTime.now());

        imageMapper.insert(image);
        return toDto(image);
    }

    public PageContent<ImageDto> list(int pageNo, int pageSize, boolean includeDeleted) {
        Page<Image> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Image> wrapper = new LambdaQueryWrapper<>();
        if (!includeDeleted) {
            wrapper.eq(Image::getDeleted, false);
        }
        wrapper.orderByDesc(Image::getCreatedAt);
        Page<Image> imagePage = imageMapper.selectPage(page, wrapper);

        List<ImageDto> dtos = new ArrayList<>(imagePage.getRecords().size());
        for (Image image : imagePage.getRecords()) {
            dtos.add(toDto(image));
        }
        Pagination pagination = new Pagination((int) imagePage.getCurrent(), (int) imagePage.getSize(), imagePage.getTotal());
        return new PageContent<>(pagination, dtos);
    }

    public ImageDto get(Long id) {
        Image image = imageMapper.selectById(id);
        if (image == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "?????");
        }
        return toDto(image);
    }

    public void softDelete(Long id) {
        Image image = imageMapper.selectById(id);
        if (image == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "?????");
        }
        if (!Boolean.TRUE.equals(image.getDeleted())) {
            image.setDeleted(Boolean.TRUE);
            imageMapper.updateById(image);
        }
    }

    public ImageContent loadImageContent(Long id, String variant) {
        Image image = imageMapper.selectById(id);
        if (image == null || Boolean.TRUE.equals(image.getDeleted())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "?????");
        }
        String normalizedVariant = normalizeVariant(variant);
        String fileName = getVariantFileName(image.getExt(), normalizedVariant);
        Path filePath = Paths.get(storageRoot).resolve(image.getBaseKey()).resolve(fileName);
        if (!Files.exists(filePath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "???????");
        }
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "??????", e);
        }

        String mimeType;
        try {
            mimeType = Files.probeContentType(filePath);
        } catch (IOException e) {
            mimeType = null;
        }
        if (!StringUtils.hasText(mimeType)) {
            mimeType = image.getMimeType();
        }
        return new ImageContent(resource, mimeType, fileName);
    }

    private void generateVariants(BufferedImage original, String ext, Path baseDir) {
        for (Integer percentage : VARIANT_PERCENTAGES) {
            double scale = percentage / 100.0;
            BufferedImage scaled = scaleImage(original, scale, ext);
            Path variantPath = baseDir.resolve(percentage + "." + ext);
            writeImage(scaled, ext, variantPath);
        }
    }

    private BufferedImage scaleImage(BufferedImage source, double scale, String ext) {
        int targetWidth = Math.max(1, (int) Math.round(source.getWidth() * scale));
        int targetHeight = Math.max(1, (int) Math.round(source.getHeight() * scale));
        BufferedImage output = createCompatibleImage(targetWidth, targetHeight, ext, source.getType());
        Graphics2D graphics = output.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawImage(source, 0, 0, targetWidth, targetHeight, null);
        } finally {
            graphics.dispose();
        }
        return output;
    }

    private BufferedImage createCompatibleImage(int width, int height, String ext, int sourceType) {
        String lowerExt = ext.toLowerCase(Locale.ROOT);
        int imageType;
        if ("jpg".equals(lowerExt) || "jpeg".equals(lowerExt)) {
            imageType = BufferedImage.TYPE_INT_RGB;
        } else if (sourceType == BufferedImage.TYPE_BYTE_GRAY) {
            imageType = BufferedImage.TYPE_BYTE_GRAY;
        } else {
            imageType = BufferedImage.TYPE_INT_ARGB;
        }
        return new BufferedImage(width, height, imageType);
    }

    private void writeImage(BufferedImage image, String ext, Path target) {
        String format = ext.toLowerCase(Locale.ROOT);
        try (OutputStream outputStream = Files.newOutputStream(target)) {
            if ("jpg".equals(format) || "jpeg".equals(format)) {
                writeJpeg(image, outputStream);
            } else {
                ImageIO.write(image, format, outputStream);
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "????????", e);
        }
    }

    private void writeJpeg(BufferedImage image, OutputStream outputStream) throws IOException {
        ImageWriter writer = null;
        ImageOutputStream ios = null;
        try {
            writer = ImageIO.getImageWritersByFormatName("jpg").next();
            ios = ImageIO.createImageOutputStream(outputStream);
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.95f);
            }
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            if (writer != null) {
                writer.dispose();
            }
            if (ios != null) {
                ios.close();
            }
        }
    }

    private String normalizeVariant(String variant) {
        if (!StringUtils.hasText(variant) || "original".equalsIgnoreCase(variant)) {
            return "original";
        }
        for (Integer percentage : VARIANT_PERCENTAGES) {
            if (percentage.toString().equals(variant)) {
                return percentage.toString();
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "?????????: " + variant);
    }

    private String getVariantFileName(String ext, String variant) {
        if ("original".equals(variant)) {
            return "original." + ext;
        }
        return variant + "." + ext;
    }

    private String computeSha256(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(bytes);
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 ????", e);
        }
    }

    private String resolveExtension(String originalName, String mimeType) {
        String ext = StringUtils.getFilenameExtension(originalName);
        if (!StringUtils.hasText(ext)) {
            ext = mimeTypeToExt(mimeType);
        }
        if (!StringUtils.hasText(ext)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "????????");
        }
        return ext.toLowerCase(Locale.ROOT);
    }

    private String mimeTypeToExt(String mimeType) {
        if (!StringUtils.hasText(mimeType)) {
            return null;
        }
        return switch (mimeType.toLowerCase(Locale.ROOT)) {
            case "image/jpeg", "image/jpg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            case "image/webp" -> "webp";
            case "image/bmp" -> "bmp";
            default -> null;
        };
    }

    private String buildBaseKey(String sha256) {
        String prefix = sha256.substring(0, 2);
        String yearMonth = YEAR_MONTH_FORMAT.format(LocalDate.now());
        return yearMonth + "/" + prefix + "/" + sha256;
    }

    private ImageDto toDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setOriginalName(image.getOriginalName());
        dto.setMimeType(image.getMimeType());
        dto.setSizeBytes(image.getSizeBytes() == null ? 0L : image.getSizeBytes());
        dto.setWidth(image.getWidth() == null ? 0 : image.getWidth());
        dto.setHeight(image.getHeight() == null ? 0 : image.getHeight());
        dto.setExt(image.getExt());
        String baseKey = Optional.ofNullable(image.getBaseKey()).orElse("").replace('\\', '/');
        dto.setBaseKey(baseKey);
        String originalRelative = buildRelativePath(baseKey, "original." + image.getExt());
        dto.setOriginalPath(originalRelative);
        dto.setOriginalUrl(buildPublicUrl(originalRelative));
        Map<String, String> variantPaths = new LinkedHashMap<>();
        Map<String, String> variantUrls = new LinkedHashMap<>();
        for (Integer percentage : VARIANT_PERCENTAGES) {
            String key = percentage.toString();
            String relativePath = buildRelativePath(baseKey, key + "." + image.getExt());
            variantPaths.put(key, relativePath);
            String url = buildPublicUrl(relativePath);
            if (url != null) {
                variantUrls.put(key, url);
            }
        }
        dto.setVariantPaths(variantPaths);
        dto.setVariantUrls(variantUrls);
        dto.setCreatedAt(image.getCreatedAt());
        return dto;
    }

    private String buildRelativePath(String baseKey, String fileName) {
        String normalizedBase = baseKey;
        if (normalizedBase.startsWith("/")) {
            normalizedBase = normalizedBase.substring(1);
        }
        if (normalizedBase.endsWith("/")) {
            normalizedBase = normalizedBase.substring(0, normalizedBase.length() - 1);
        }
        if (!StringUtils.hasText(normalizedBase)) {
            return fileName;
        }
        return normalizedBase + "/" + fileName;
    }

    private String buildPublicUrl(String relativePath) {
        if (!StringUtils.hasText(publicBaseUrl)) {
            return null;
        }
        String base = publicBaseUrl.endsWith("/") ? publicBaseUrl.substring(0, publicBaseUrl.length() - 1) : publicBaseUrl;
        String relative = relativePath.startsWith("/") ? relativePath.substring(1) : relativePath;
        return base + "/" + relative;
    }

    public static class ImageContent {
        private final Resource resource;
        private final String mimeType;
        private final String filename;

        public ImageContent(Resource resource, String mimeType, String filename) {
            this.resource = resource;
            this.mimeType = mimeType;
            this.filename = filename;
        }

        public Resource getResource() {
            return resource;
        }

        public String getMimeType() {
            return mimeType;
        }

        public String getFilename() {
            return filename;
        }
    }
}



