package com.neusoft.community.admin.controller;

import com.neusoft.community.admin.service.AdminDashboardService;
import com.neusoft.community.admin.vo.DashboardOverviewVO;
import com.neusoft.community.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin - 首页仪表盘接口")
@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @Operation(summary = "获取首页仪表盘汇总数据")
    @GetMapping("/overview")
    public Result<DashboardOverviewVO> getDashboardOverview() {
        return adminDashboardService.getDashboardOverview();
    }


    @Operation(summary = "获取最新系统公告")
    @GetMapping("/latest-notices")
    public Result<?> getLatestNotices() {
        return adminDashboardService.getLatestNotices();
    }
}
