package com.yu.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.blog.entity.Label;
import com.yu.blog.vo.LabelVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LabelDao extends BaseMapper<Label> {
    List<LabelVO> selectLabelList();
}
