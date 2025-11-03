package com.yu.blog.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DeleteCategoryRequest {

    @NotNull(message = "分类ID不能为空")
    @Positive(message = "分类ID必须为正数")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
