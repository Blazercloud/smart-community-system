package com.neusoft.community.user.controller;


import com.neusoft.community.admin.dto.ParkingSpaceDTO;
import com.neusoft.community.admin.service.ParkingSpaceService;
import com.neusoft.community.admin.vo.ParkingSpaceVO;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/community/parking")
@RestController
public class UserParkSpaceController {
    @Autowired
    private ParkingSpaceService ParkingSpaceService;
    
    /**
     * 分页查询车位信息，支持根据车主ID和车牌号模糊查询
     */
    @GetMapping("/info")
    public Result<PageResult<List<ParkingSpaceVO>>> getParkingInfo(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long Id,
            @RequestParam(required = false) String carNumber){
        return ParkingSpaceService.getParkingInfo(currentPage, pageSize, Id, carNumber);
    }
    

}