package com.yu.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.blog.entity.Blogs;
import com.yu.blog.vo.BlogCovers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogsDao extends BaseMapper<Blogs> {
    List<BlogCovers> selectAllBlogs(@Param("keyword") String keyword,
                                     @Param("categoryId") Integer categoryId,
                                     @Param("labelId") Integer labelId);
}