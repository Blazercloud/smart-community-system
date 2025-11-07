package com.neusoft.community.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.community.admin.entity.Visitor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VisitorMapper extends BaseMapper<Visitor> {
    // 自定义查询可在这里扩展
}
