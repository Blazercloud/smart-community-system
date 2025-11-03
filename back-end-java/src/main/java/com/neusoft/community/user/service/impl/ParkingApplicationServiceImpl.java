package com.neusoft.community.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.community.common.Result;
import com.neusoft.community.user.entity.ParkingApplication;
import com.neusoft.community.user.service.ParkingApplicationService;
import com.neusoft.community.user.mapper.ParkingApplicationMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author hui
* @description 针对表【parking_application】的数据库操作Service实现
* @createDate 2025-11-03 11:44:42
*/
@Service
public class ParkingApplicationServiceImpl extends ServiceImpl<ParkingApplicationMapper, ParkingApplication>
    implements ParkingApplicationService{

    @Override
    public Result<Void> createParkApplication(ParkingApplication parkingApplication) {
        return this.save(parkingApplication)? Result.success("创建申请成功") : Result.fail() ;
    }

    @Override
    public Result<List<ParkingApplication>> getParkingApplication(Integer userId) {
        QueryWrapper<ParkingApplication> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<ParkingApplication> parkingApplicationlist = this.list(wrapper);
        return parkingApplicationlist == null ? Result.fail("无停车申请") : Result.success(parkingApplicationlist);
    }
}




