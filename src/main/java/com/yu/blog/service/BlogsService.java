package com.yu.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yu.blog.dao.BlogsDao;
import com.yu.blog.dao.CategoryDao;
import com.yu.blog.dao.LabelDao;
import com.yu.blog.entity.Blogs;
import com.yu.blog.vo.BlogCovers;
import com.yu.blog.vo.BlogListVO;
import com.yu.blog.vo.PanelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogsService {

    @Autowired
    private  BlogsDao blogsDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private LabelDao labelDao;

    public BlogListVO getBlogList(String keyword, Integer categoryId, Integer labelId, Integer pageNum, Integer pageSize){
        // 启动分页功能
        PageHelper.startPage(pageNum, pageSize);

        // 查询数据
        List<BlogCovers> list = blogsDao.selectAllBlogs(keyword, categoryId, labelId);

        // 封装分页信息
        PageInfo<BlogCovers> pageInfo = new PageInfo<>(list);

        // 构建返回对象
        BlogListVO blogListVO = new BlogListVO();
        blogListVO.setList(pageInfo.getList());
        blogListVO.setPageNum(pageInfo.getPageNum());
        blogListVO.setPageSize(pageInfo.getPageSize());
        blogListVO.setTotal(pageInfo.getTotal());
        blogListVO.setPages(pageInfo.getPages());

        return blogListVO;
    }

    public Blogs detail(Integer id) {
        return blogsDao.selectOne(
                new QueryWrapper<Blogs>().eq("id", id));
    }

    public PanelVO getPanelData() {
        PanelVO panelVO = new PanelVO();

        // 统计博客数量
        Long blogCount = blogsDao.selectCount(null);
        panelVO.setBlogCount(blogCount);

        // 统计分类数量
        Long categoryCount = categoryDao.selectCount(null);
        panelVO.setCategoryCount(categoryCount);

        // 统计标签数量
        Long labelCount = labelDao.selectCount(null);
        panelVO.setLabelCount(labelCount);

        return panelVO;
    }

    public Long getBlogCount() {
        return blogsDao.selectCount(null);
    }
}
