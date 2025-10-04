package com.yu.blog.bean;

/**
 * @Author: Mr.Chang
 * @Date: 2022/5/25
 */
public class Pagination {
    private int pageNo = 1;
    private int pageSize = 10;
    private long total;

    public Pagination() {
    }

    public Pagination(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Pagination(int pageNo, int pageSize, long total) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getStartRecord() {
        return (this.pageNo - 1) * this.pageSize;
    }

    public int getEndRecord() {
        return this.pageNo * this.pageSize;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Pagination [pageNo=");
        builder.append(this.pageNo);
        builder.append(", pageSize=");
        builder.append(this.pageSize);
        builder.append(", total=");
        builder.append(this.total);
        builder.append("]");
        return builder.toString();
    }
}