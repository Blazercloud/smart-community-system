package com.neusoft.community.admin.controller;

import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.common.annotation.NoAuth;
import com.neusoft.community.user.entity.Repair;
import com.neusoft.community.user.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/repair")
public class RepairApplicationController {
    @Autowired
    private RepairService repairService;


    @GetMapping
    public Result<PageResult<Repair>> getAllRepair(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        return repairService.getAllRepair(currentPage, pageSize);
    }

    @PutMapping
    public Result<Void> updateRepair(@RequestBody Repair repair){
        return repairService.updateRepair(repair);
    }
}