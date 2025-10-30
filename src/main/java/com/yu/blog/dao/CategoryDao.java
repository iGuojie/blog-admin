package com.yu.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.blog.entity.Blogs;
import com.yu.blog.entity.Category;
import com.yu.blog.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryDao extends BaseMapper<Category> {
    List<CategoryVO> selectCategoryList();
}
