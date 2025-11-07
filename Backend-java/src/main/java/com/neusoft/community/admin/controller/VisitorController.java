package com.neusoft.community.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.community.admin.entity.Visitor;
import com.neusoft.community.admin.service.VisitorService;
import com.neusoft.community.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/property/visitor")
@Tag(name = "访客管理")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    @GetMapping("/list")
    @Operation(summary = "分页获取访客列表")
    public Result list(@RequestParam(defaultValue = "1") Long currentPage,
                       @RequestParam(defaultValue = "10") Long pageSize,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String type) {
        Page<Visitor> page = new Page<>(currentPage, pageSize);
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("type", type);

        Page<Visitor> visitorPage = visitorService.getVisitorList(page, params);
        return Result.success(visitorPage);
    }

    @PostMapping("/add")
    @Operation(summary = "添加访客")
    public Result add(@RequestBody Visitor visitor) {
        visitorService.addVisitor(visitor);
        return Result.success("添加成功");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除访客")
    public Result delete(@PathVariable Integer id) {
        visitorService.deleteVisitor(id);
        return Result.success("删除成功");
    }
}
