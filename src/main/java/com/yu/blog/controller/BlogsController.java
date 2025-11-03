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
    public RtnData getBlogList(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "labelId", required = false) Integer labelId,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "6") Integer pageSize) {
        return RtnData.ok(blogsService.getBlogList(keyword, categoryId, labelId, pageNum, pageSize));
    }

    @GetMapping("/detail")
    public RtnData detail(@RequestParam("id") Integer id) {
        return RtnData.ok(blogsService.detail(id));
    }

    @GetMapping("/panel")
    public RtnData panel() {
        return RtnData.ok(blogsService.getPanelData());
    }

    @GetMapping("/count")
    public RtnData getBlogCount() {
        return RtnData.ok(blogsService.getBlogCount());
    }

}
