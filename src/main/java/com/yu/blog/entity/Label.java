package com.yu.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("label")
public class Label {
    private Integer id;
    private String name;
    private String descirption;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescirption() {
        return descirption;
    }

    public void setDescirption(String descirption) {
        this.descirption = descirption;
    }
}
