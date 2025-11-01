package com.yu.blog.service;

import com.yu.blog.dao.LabelDao;
import com.yu.blog.vo.LabelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    public List<LabelVO> getLabelList() {
        return labelDao.selectLabelList();
    }
}