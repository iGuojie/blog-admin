package com.yu.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yu.blog.bean.PageContent;
import com.yu.blog.bean.Pagination;
import com.yu.blog.dao.LabelDao;
import com.yu.blog.entity.Label;
import com.yu.blog.request.AddLabelRequest;
import com.yu.blog.request.UpdateLabelRequest;
import com.yu.blog.vo.LabelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    // 获取所有标签列表（不分页，前台用）
    public List<LabelVO> getAllLabelList() {
        return labelDao.selectLabelList();
    }

    // 获取标签列表（分页，后台管理用）
    public PageContent<LabelVO> getLabelList(Integer pageNo, Integer pageSize) {
        // 使用PageHelper进行分页
        PageHelper.startPage(pageNo, pageSize);
        List<LabelVO> labels = labelDao.selectLabelList();
        PageInfo<LabelVO> pageInfo = new PageInfo<>(labels);

        // 构建分页信息
        Pagination pagination = new Pagination(pageNo, pageSize);
        pagination.setTotal(pageInfo.getTotal());

        return new PageContent<>(pagination, labels);
    }

    public void addLabel(AddLabelRequest request) {
        Label label = new Label();
        label.setName(request.getName());
        label.setDescirption(request.getDescirption());
        labelDao.insert(label);
    }

    public void updateLabel(UpdateLabelRequest request) {
        Label label = labelDao.selectById(request.getId());
        if (label == null) {
            throw new RuntimeException("标签不存在");
        }
        label.setName(request.getName());
        label.setDescirption(request.getDescirption());
        labelDao.updateById(label);
    }

    public void deleteLabel(Integer labelId) {
        Label label = labelDao.selectById(labelId);
        if (label == null) {
            throw new RuntimeException("标签不存在");
        }

        Integer blogCount = labelDao.countBlogsByLabelId(labelId);
        if (blogCount != null && blogCount > 0) {
            throw new RuntimeException("该标签下还有 " + blogCount + " 篇文章，无法删除");
        }

        labelDao.deleteById(labelId);
    }
}