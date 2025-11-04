package com.neusoft.community.user.controller;

import com.neusoft.community.common.Result;
import com.neusoft.community.common.annotation.NoAuth;
import com.neusoft.community.user.entity.ParkingApplication;
import com.neusoft.community.user.service.ParkingApplicationService;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user/park/application")
@Tag(name = "停车申请管理")
public class UserParkApplicationController {

    @Autowired
    ParkingApplicationService ParkingApplicationService;

    @NoAuth
    @PostMapping
    @Operation(summary = "用户创建停车申请")
    public Result<Void> createParkApplication(@RequestBody ParkingApplication parkingApplication){
        parkingApplication.setStatus("0");

        return ParkingApplicationService.createParkApplication(parkingApplication);
    }


    @NoAuth
    @Operation(summary = "获取用户停车申请")
    @GetMapping
    public Result<List<ParkingApplication>> getParkingApplication(@Param("Id") Integer userId){
        return ParkingApplicationService.getParkingApplication(userId);
    }


}
