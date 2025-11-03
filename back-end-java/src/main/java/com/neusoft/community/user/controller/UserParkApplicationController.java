package com.neusoft.community.user.controller;

import com.neusoft.community.common.Result;
import com.neusoft.community.common.annotation.NoAuth;
import com.neusoft.community.user.entity.ParkingApplication;
import com.neusoft.community.user.service.ParkingApplicationService;
import io.lettuce.core.dynamic.annotation.Param;
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

    @PostMapping
    public Result<Void> createParkApplication(@RequestBody ParkingApplication parkingApplication){
        parkingApplication.setStatus("0");

        return ParkingApplicationService.createParkApplication(parkingApplication);
    }


    @NoAuth
    @GetMapping
    public Result<List<ParkingApplication>> getParkingApplication(@Param("userId") Integer userId){
        return ParkingApplicationService.getParkingApplication(userId);
    }


}
