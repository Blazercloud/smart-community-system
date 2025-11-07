package com.neusoft.community.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.community.admin.entity.Visitor;
import com.neusoft.community.admin.mapper.VisitorMapper;
import com.neusoft.community.admin.service.VisitorService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements VisitorService {

    @Override
    public Page<Visitor> getVisitorList(Page<Visitor> page, Map<String, Object> params) {
        String keyword = (String) params.get("keyword");
        String type = (String) params.get("type");

        LambdaQueryWrapper<Visitor> query = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            query.and(q -> q.like(Visitor::getName, keyword)
                    .or()
                    .like(Visitor::getPurpose, keyword));
        }
        if (StringUtils.hasText(type)) {
            query.eq(Visitor::getVisitorType, type);
        }

        query.orderByDesc(Visitor::getVisitTime);
        return this.page(page, query);
    }

    @Override
    public void addVisitor(Visitor visitor) {
        this.save(visitor);
    }

    @Override
    public void deleteVisitor(Integer id) {
        this.removeById(id);
    }
}
