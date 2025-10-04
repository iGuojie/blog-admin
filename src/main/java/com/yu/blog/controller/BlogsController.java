package com.yu.blog.controller;

import com.yu.blog.bean.RtnData;
import com.yu.blog.service.BlogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blogs")
public class BlogsController {

    @Autowired
    private BlogsService blogsService;

    @GetMapping("/getBlogList")
    public RtnData getBlogList() {
        return RtnData.ok(blogsService.getBlogList());
    }

    @GetMapping("/detail")
    public RtnData detail(@RequestParam("id") Integer id) {
        return RtnData.ok(blogsService.detail(id));
    }

}
