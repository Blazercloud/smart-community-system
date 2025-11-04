package com.neusoft.community.admin.controller;

import com.neusoft.community.admin.entity.ParkingSpace;
import com.neusoft.community.admin.mapper.ParkingSpaceMapper;
import com.neusoft.community.admin.vo.ParkingApplicationVO;
import com.neusoft.community.common.Result;
import com.neusoft.community.user.entity.ParkingApplication;
import com.neusoft.community.user.entity.User;
import com.neusoft.community.user.mapper.UserMapper;
import com.neusoft.community.user.service.ParkingApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@RestController
@RequestMapping("/admin/parking/application")
@Tag(name = "停车申请管理(管理员端)")
public class ParkingApplicationController {

    @Autowired
    private ParkingApplicationService parkingApplicationService;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ParkingSpaceMapper parkingSpaceMapper;

    /**
     * 获取所有停车申请（带用户信息）
     */
    @GetMapping
    @Operation(summary = "获取所有停车申请")
    public Result<List<ParkingApplicationVO>> getAllParkingApplications() {
        // 获取所有申请
        List<ParkingApplication> applications = parkingApplicationService.list();
        
        // 转换为VO对象并添加用户信息
        List<ParkingApplicationVO> applicationVOs = applications.stream().map(application -> {
            ParkingApplicationVO vo = new ParkingApplicationVO();
            vo.setId(application.getId());
            vo.setUserId(application.getUserId());
            vo.setSpaceNumber(application.getSpaceNumber());
            vo.setCarNumber(application.getCarNumber());
            vo.setStatus(application.getStatus());
            vo.setApplyTime(application.getApplyTime());
            
            // 查询用户信息
            User user = userMapper.selectById(application.getUserId());
            if (user != null) {
                vo.setUsername(user.getUsername());
            }
            
            return vo;
        }).collect(Collectors.toList());
        
        return Result.success(applicationVOs);
    }

    /**
     * 更新停车申请状态
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新停车申请状态")
    public Result<String> updateParkingApplicationStatus(
            @PathVariable Integer id,
            @RequestBody ParkingApplication application) {
        // 获取原始申请信息
        ParkingApplication existingApplication = parkingApplicationService.getById(id);
        if (existingApplication == null) {
            return Result.fail("申请记录不存在");
        }

        // 更新状态
        existingApplication.setStatus(application.getStatus());
        boolean success = parkingApplicationService.updateById(existingApplication);
        
        // 如果申请被同意，则更新车位信息
        if (success && "1".equals(application.getStatus())) {
            // 根据车位号查找车位
            ParkingSpace parkingSpace = parkingSpaceMapper.selectOne(
                new QueryWrapper<ParkingSpace>().eq("space_number", existingApplication.getSpaceNumber())
            );
            
            if (parkingSpace != null) {
                // 更新车位的车主信息
                parkingSpace.setOwnerId(existingApplication.getUserId());
                parkingSpace.setCarNumber(existingApplication.getCarNumber());
                parkingSpace.setStatus("已占用"); // 更新车位状态为已占用
                parkingSpaceMapper.updateById(parkingSpace);
            }
        }
        
        if (success) {
            return Result.success("申请状态更新成功");
        } else {
            return Result.fail("申请状态更新失败");
        }
    }
}