package com.neusoft.community.user.service;

import com.neusoft.community.admin.vo.ParkingApplicationVO;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.user.entity.ParkingApplication;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author hui
* @description 针对表【parking_application】的数据库操作Service
* @createDate 2025-11-03 11:44:42
*/
public interface ParkingApplicationService extends IService<ParkingApplication> {

    Result<Void> createParkApplication(ParkingApplication parkingApplication);

    /**
     * 获取用户自己的停车申请
     */
    Result<List<ParkingApplication>> getParkingApplication(Integer userId);

    /**
     * 获取所有停车申请（带用户信息）
     */
    Result<PageResult<List<ParkingApplicationVO>>> getAllParkingApplication(Integer currentPage, Integer pageSize, String status);

    Result<Void> updateParkingApplicationStatus(Integer id, ParkingApplication application);
}
