package com.yu.blog.controller;

import com.yu.blog.bean.RtnData;
import com.yu.blog.service.ImageService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public RtnData upload(@RequestPart("file") MultipartFile file) {
        return RtnData.ok(imageService.upload(file));
    }

    @GetMapping
    public RtnData list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                        @RequestParam(value = "includeDeleted", defaultValue = "false") boolean includeDeleted) {
        return RtnData.ok(imageService.list(pageNo, pageSize, includeDeleted));
    }

    @GetMapping("/{id}")
    public RtnData detail(@PathVariable("id") Long id) {
        return RtnData.ok(imageService.get(id));
    }

    @DeleteMapping("/{id}")
    public RtnData remove(@PathVariable("id") Long id) {
        imageService.softDelete(id);
        return RtnData.ok();
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable("id") Long id,
                                      @RequestParam(value = "variant", defaultValue = "original") String variant) {
        ImageService.ImageContent content = imageService.loadImageContent(id, variant);
        String mimeType = content.getMimeType();
        MediaType mediaType = StringUtils.hasText(mimeType) ? MediaType.parseMediaType(mimeType) : MediaType.APPLICATION_OCTET_STREAM;
        ContentDisposition disposition = ContentDisposition.inline()
                .filename(content.getFilename(), StandardCharsets.UTF_8)
                .build();
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(content.getResource());
    }
}
