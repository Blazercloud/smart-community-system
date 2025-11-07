package com.neusoft.community.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.community.admin.entity.Visitor;

import java.util.Map;

public interface VisitorService {

    Page<Visitor> getVisitorList(Page<Visitor> page, Map<String, Object> params);

    void addVisitor(Visitor visitor);

    void deleteVisitor(Integer id);
}
