package com.neusoft.community.admin.service;

import com.neusoft.community.admin.dto.ParkingSpaceDTO;
import com.neusoft.community.admin.entity.ParkingSpace;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.community.admin.vo.ParkingSpaceVO;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;

import java.util.List;

/**
* @author hui
* @description 针对表【parking_space】的数据库操作Service
* @createDate 2025-11-02 16:33:12
*/
public interface ParkingSpaceService extends IService<ParkingSpace> {

     Result<PageResult<List<ParkingSpaceVO>>> getParkingInfo(Integer currentPage, Integer pageSize, Long Id, String carNumber) ;

     Result<String> addParkingSpace(ParkingSpaceDTO parkingSpaceDTO);

     Result<Void> updateParkingSpace(ParkingSpaceDTO parkingSpaceDTO);
}