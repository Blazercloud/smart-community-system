package com.neusoft.community.admin.service.impl;

import com.neusoft.community.admin.entity.Visitor;
import com.neusoft.community.admin.mapper.VisitorMapper;
import com.neusoft.community.admin.service.VisitorService;
import com.neusoft.community.admin.vo.VisitorVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class VisitorServiceImpl implements VisitorService {

    @Resource
    private VisitorMapper visitorMapper;

    @Override
    public List<VisitorVO> getVisitorVOList(int currentPage, int pageSize, Map<String, Object> params) {
        int offset = (currentPage - 1) * pageSize;
        String keyword = (String) params.get("keyword");
        String type = (String) params.get("type");
        return visitorMapper.selectVisitorVOList(offset, pageSize, keyword, type);
    }

    @Override
    public long countVisitorVO(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");
        String type = (String) params.get("type");
        return visitorMapper.countVisitorVOList(keyword, type);
    }

    @Override
    public void addVisitor(Visitor visitor) {
        // MyBatis-Plus 自带 insert 方法
        visitorMapper.insert(visitor);
    }

    @Override
    public void deleteVisitor(Integer id) {
        // MyBatis-Plus 自带 deleteById 方法
        visitorMapper.deleteById(id);
    }
}
