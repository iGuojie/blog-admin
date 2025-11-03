package com.yu.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yu.blog.bean.PageContent;
import com.yu.blog.bean.Pagination;
import com.yu.blog.dao.CategoryDao;
import com.yu.blog.entity.Category;
import com.yu.blog.request.AddCategoryRequest;
import com.yu.blog.request.UpdateCategoryRequest;
import com.yu.blog.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    // 获取所有分类列表（不分页，前台用）
    public List<CategoryVO> getAllCategoryList() {
        return categoryDao.selectCategoryList();
    }

    // 获取分类列表（分页，后台管理用）
    public PageContent<CategoryVO> getCategoryList(Integer pageNo, Integer pageSize) {
        // 使用PageHelper进行分页
        PageHelper.startPage(pageNo, pageSize);
        List<CategoryVO> categories = categoryDao.selectCategoryList();
        PageInfo<CategoryVO> pageInfo = new PageInfo<>(categories);

        // 构建分页信息
        Pagination pagination = new Pagination(pageNo, pageSize);
        pagination.setTotal(pageInfo.getTotal());

        return new PageContent<>(pagination, categories);
    }

    public void addCategory(AddCategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        categoryDao.insert(category);
    }

    public void updateCategory(UpdateCategoryRequest request) {
        Category category = categoryDao.selectById(request.getId());
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        categoryDao.updateById(category);
    }

    public void deleteCategory(Integer categoryId) {
        Category category = categoryDao.selectById(categoryId);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }

        Integer blogCount = categoryDao.countBlogsByCategoryId(categoryId);
        if (blogCount != null && blogCount > 0) {
            throw new RuntimeException("该分类下还有 " + blogCount + " 篇文章，无法删除");
        }

        categoryDao.deleteById(categoryId);
    }
}
