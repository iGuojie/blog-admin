package com.yu.blog.bean;

import java.util.List;

/**
 * @Author: Mr.Chang
 * @Date: 2022/5/25
 * @description:
 * 分页对象
 * 包括分页信息与分页数据
 */
public class PageContent<T> {

    /**
     * 分页信息
     */
    private Pagination page;

    /**
     * 数据
     */
    private List<T> list;

    public PageContent(Pagination pagination, List<T> list) {
        this.page = pagination;
        this.list = list;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
