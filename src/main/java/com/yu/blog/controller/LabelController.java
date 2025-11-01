package com.yu.blog.controller;

import com.yu.blog.bean.RtnData;
import com.yu.blog.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/label")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @GetMapping("/getLabel")
    public RtnData getLabel() {
        return RtnData.ok(labelService.getLabelList());
    }

}
