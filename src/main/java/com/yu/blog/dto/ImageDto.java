package com.yu.blog.dto;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImageDto {
    private Long id;
    private String originalName;
    private String mimeType;
    private long sizeBytes;
    private int width;
    private int height;
    private String ext;
    private String baseKey;
    private String originalPath;
    private String originalUrl;
    private Map<String, String> variantPaths = new LinkedHashMap<>();
    private Map<String, String> variantUrls = new LinkedHashMap<>();
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getBaseKey() {
        return baseKey;
    }

    public void setBaseKey(String baseKey) {
        this.baseKey = baseKey;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Map<String, String> getVariantPaths() {
        return variantPaths;
    }

    public void setVariantPaths(Map<String, String> variantPaths) {
        this.variantPaths = variantPaths == null ? new LinkedHashMap<>() : new LinkedHashMap<>(variantPaths);
    }

    public Map<String, String> getVariantUrls() {
        return variantUrls;
    }

    public void setVariantUrls(Map<String, String> variantUrls) {
        this.variantUrls = variantUrls == null ? new LinkedHashMap<>() : new LinkedHashMap<>(variantUrls);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
