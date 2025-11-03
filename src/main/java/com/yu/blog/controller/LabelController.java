package com.yu.blog.controller;

import com.yu.blog.bean.RtnData;
import com.yu.blog.request.AddLabelRequest;
import com.yu.blog.request.DeleteLabelRequest;
import com.yu.blog.request.UpdateLabelRequest;
import com.yu.blog.service.LabelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/label")
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * 获取所有标签列表（不分页，前台用）
     */
    @GetMapping("/getLabel")
    public RtnData getLabel() {
        return RtnData.ok(labelService.getAllLabelList());
    }

    /**
     * 获取标签列表（分页，后台管理用）
     */
    @GetMapping("/list")
    public RtnData getLabelListWithPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        return RtnData.ok(labelService.getLabelList(pageNo, pageSize));
    }

    @PostMapping("/add")
    public RtnData addLabel(@Valid @RequestBody AddLabelRequest request) {
        try {
            labelService.addLabel(request);
            return RtnData.ok("标签添加成功");
        } catch (Exception e) {
            return RtnData.fail(e.getMessage());
        }
    }

    @PostMapping("/update")
    public RtnData updateLabel(@Valid @RequestBody UpdateLabelRequest request) {
        try {
            labelService.updateLabel(request);
            return RtnData.ok("标签更新成功");
        } catch (Exception e) {
            return RtnData.fail(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public RtnData deleteLabel(@Valid @RequestBody DeleteLabelRequest request) {
        try {
            labelService.deleteLabel(request.getId());
            return RtnData.ok("标签删除成功");
        } catch (Exception e) {
            return RtnData.fail(e.getMessage());
        }
    }

}
