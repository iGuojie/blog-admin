package com.yu.blog.controller;

import com.yu.blog.bean.RtnData;
import com.yu.blog.service.BlogsService;
import com.yu.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategory")
    public RtnData getCategory() {
        return RtnData.ok(categoryService.getCategoryList());
    }

}
