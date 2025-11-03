package com.neusoft.community.admin.controller;


import com.neusoft.community.admin.entity.ParkingSpace;
import com.neusoft.community.admin.service.ParkingSpaceService;
import com.neusoft.community.admin.vo.ParkingSpaceVO;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/community/parking")
@RestController
public class ParkSpaceController {
    @Autowired
    private ParkingSpaceService ParkingSpaceService;

    @GetMapping("/info")
    public Result<PageResult<List<ParkingSpaceVO>>> getParkingInfo(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer id){
        return ParkingSpaceService.getParkingInfo(currentPage, pageSize, id);
    }
}