package com.yu.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yu.blog.dao.BlogsDao;
import com.yu.blog.entity.Blogs;
import com.yu.blog.vo.BlogCovers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogsService {

    @Autowired
    private  BlogsDao blogsDao;

    public List<BlogCovers> getBlogList(){
        return blogsDao.selectAllBlogsCover();
    }

    public Blogs detail(Integer id) {
        return blogsDao.selectOne(
                new QueryWrapper<Blogs>().eq("id", id));
    }
}
