package com.neusoft.community.user.controller;

import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.common.annotation.NoAuth;
import com.neusoft.community.user.entity.Repair;
import com.neusoft.community.user.service.RepairService;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/repair")
public class UserRepairApplicationController {

    @Autowired
    private RepairService repairService;

    @NoAuth
    @PostMapping
    public Result<Void> applyRepair(@Valid @RequestBody Repair repair) {return repairService.applyRepair(repair);


    }

@NoAuth
    @GetMapping
    public Result<PageResult<List<Repair>>> getRepair(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(value = "userId" ) Integer userId
    ){
        return repairService.getRepair(currentPage, pageSize, userId);
    }
}
