package com.neusoft.community.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.community.admin.entity.Visitor;
import com.neusoft.community.admin.vo.VisitorVO;
import com.neusoft.community.admin.service.VisitorService;
import com.neusoft.community.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/property/visitor")
@Tag(name = "访客管理")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") int currentPage,
                       @RequestParam(defaultValue = "10") int pageSize,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String type) {

        Map<String, Object> params = Map.of("keyword", keyword, "type", type);

        List<VisitorVO> list = visitorService.getVisitorVOList(currentPage, pageSize, params);
        long total = visitorService.countVisitorVO(params);

        Map<String, Object> result = Map.of(
                "records", list,
                "total", total,
                "currentPage", currentPage,
                "pageSize", pageSize
        );

        return Result.success(result);
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
