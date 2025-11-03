package com.yu.blog.vo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlogCovers {
    private Integer Id;
    private String name;
    private LocalDateTime createdTime;
    private String imageUrl;
    private String abstracts;
    private String category;
    private List<String> labels;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public void setLabelsStr(String labelsStr) {
        if (labelsStr != null && !labelsStr.isEmpty()) {
            this.labels = Arrays.asList(labelsStr.split(","));
        } else {
            this.labels = new ArrayList<>();
        }
    }
}
