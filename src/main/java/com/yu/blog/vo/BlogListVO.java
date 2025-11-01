package com.yu.blog.vo;

import java.util.List;

public class BlogListVO {
    private List<BlogCovers> list;
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private Integer pages;

    public List<BlogCovers> getList() {
        return list;
    }

    public void setList(List<BlogCovers> list) {
        this.list = list;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}
