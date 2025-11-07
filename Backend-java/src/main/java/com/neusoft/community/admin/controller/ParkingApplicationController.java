package com.neusoft.community.admin.controller;

import com.neusoft.community.admin.vo.ParkingApplicationVO;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.user.entity.ParkingApplication;
import com.neusoft.community.user.service.ParkingApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/parking/application")
@Tag(name = "停车申请管理(管理员端)")
public class ParkingApplicationController {

    @Autowired
    private ParkingApplicationService parkingApplicationService;
    

    /**
     * 获取所有停车申请（带用户信息）
     */
    @GetMapping
    @Operation(summary = "获取所有停车申请")
    public Result<PageResult<ParkingApplicationVO>> getAllParkingApplications(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String status) {
        // 获取所有申请
        return parkingApplicationService.getAllParkingApplication(currentPage,pageSize,status);
    }

    /**
     * 更新停车申请状态
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新停车申请状态")
    public Result<Void> updateParkingApplicationStatus(
            @PathVariable Integer id,
            @RequestBody ParkingApplication application) {

        return parkingApplicationService.updateParkingApplicationStatus(id, application);
        // 获取原始申请信息
    }
}