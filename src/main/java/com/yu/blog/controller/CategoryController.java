package com.yu.blog.controller;

import com.yu.blog.bean.RtnData;
import com.yu.blog.request.AddCategoryRequest;
import com.yu.blog.request.DeleteCategoryRequest;
import com.yu.blog.request.UpdateCategoryRequest;
import com.yu.blog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有分类列表（不分页，前台用）
     */
    @GetMapping("/getCategory")
    public RtnData getCategory() {
        return RtnData.ok(categoryService.getAllCategoryList());
    }

    /**
     * 获取分类列表（分页，后台管理用）
     */
    @GetMapping("/list")
    public RtnData getCategoryListWithPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                           @RequestParam(defaultValue = "10") Integer pageSize) {
        return RtnData.ok(categoryService.getCategoryList(pageNo, pageSize));
    }

    @PostMapping("/add")
    public RtnData addCategory(@Valid @RequestBody AddCategoryRequest request) {
        try {
            categoryService.addCategory(request);
            return RtnData.ok("分类添加成功");
        } catch (Exception e) {
            return RtnData.fail(e.getMessage());
        }
    }

    @PostMapping("/update")
    public RtnData updateCategory(@Valid @RequestBody UpdateCategoryRequest request) {
        try {
            categoryService.updateCategory(request);
            return RtnData.ok("分类更新成功");
        } catch (Exception e) {
            return RtnData.fail(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public RtnData deleteCategory(@Valid @RequestBody DeleteCategoryRequest request) {
        try {
            categoryService.deleteCategory(request.getId());
            return RtnData.ok("分类删除成功");
        } catch (Exception e) {
            return RtnData.fail(e.getMessage());
        }
    }

}
