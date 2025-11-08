package com.neusoft.community.admin.service;

import com.neusoft.community.admin.entity.Visitor;
import com.neusoft.community.admin.vo.VisitorVO;

import java.util.List;
import java.util.Map;

public interface VisitorService {

    /**
     * 分页查询访客记录
     */
    List<VisitorVO> getVisitorVOList(int currentPage, int pageSize, Map<String, Object> params);

    /**
     * 统计访客数量
     */
    long countVisitorVO(Map<String, Object> params);

    /**
     * 添加访客
     */
    void addVisitor(Visitor visitor);

    /**
     * 删除访客
     */
    void deleteVisitor(Integer id);
}
