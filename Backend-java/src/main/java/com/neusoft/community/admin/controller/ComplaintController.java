package com.neusoft.community.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.community.admin.entity.Complaint;
import com.neusoft.community.admin.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @GetMapping
    /*
     *获取投诉列表
    */
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer isHandled) {

        LambdaQueryWrapper<Complaint> wrapper = new LambdaQueryWrapper<>();
        if (isHandled != null) wrapper.eq(Complaint::getIsHandled, isHandled);
        wrapper.orderByDesc(Complaint::getCreateTime);

        Page<Complaint> page = complaintService.page(new Page<>(current, size), wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("rows", page.getRecords());
        result.put("total", page.getTotal());
        return result;
    }

    @PutMapping
    public Map<String, Object> update(@RequestBody Complaint complaint) {
        boolean success = complaintService.updateById(complaint);
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("msg", success ? "更新成功" : "更新失败");
        return result;
    }
}
