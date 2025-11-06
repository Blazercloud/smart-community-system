package com.neusoft.community.admin.controller;


import com.neusoft.community.admin.entity.ParkingSpace;
import com.neusoft.community.admin.service.ParkingSpaceService;
import com.neusoft.community.admin.vo.ParkingSpaceVO;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/community/parking")
@RestController
public class ParkSpaceController {
    @Autowired
    private ParkingSpaceService ParkingSpaceService;
    
    /**
     * 分页查询车位信息，支持根据车主ID和车牌号模糊查询
     */
    @GetMapping("/info")
    public Result<PageResult<List<ParkingSpaceVO>>> getParkingInfo(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String carNumber){
        return ParkingSpaceService.getParkingInfo(currentPage, pageSize, id, carNumber);
    }
    
    /**
     * 更新车位信息
     */
    @PutMapping
    public Result<Void> updateParkingSpace(@RequestBody ParkingSpace parkingSpace) {
        boolean success = ParkingSpaceService.updateById(parkingSpace);
        if (success) {
            return Result.success("车位信息更新成功");
        } else {
            return Result.fail("车位信息更新失败");
        }
    }
    
    /**
     * 添加车位
     */
    @PostMapping
    public Result<String> addParkingSpace(@RequestBody ParkingSpace parkingSpace) {
        boolean success = ParkingSpaceService.save(parkingSpace);
        if (success) {
            return Result.success("车位添加成功");
        } else {
            return Result.fail("车位添加失败");
        }
    }
    
    /**
     * 删除车位
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteParkingSpace(@PathVariable Integer id) {
        boolean success = ParkingSpaceService.removeById(id);
        if (success) {
            return Result.success("车位删除成功");
        } else {
            return Result.fail("车位删除失败");
        }
    }
}