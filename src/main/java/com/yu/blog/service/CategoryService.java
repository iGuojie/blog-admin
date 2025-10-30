package com.yu.blog.service;

import com.yu.blog.dao.CategoryDao;
import com.yu.blog.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    public List<CategoryVO> getCategoryList() {
        return categoryDao.selectCategoryList();
    }
}
