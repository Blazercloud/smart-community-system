package com.neusoft.community.user.service;

import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.user.entity.Repair;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author hui
* @description 针对表【repair】的数据库操作Service
* @createDate 2025-11-05 12:01:50
*/
public interface RepairService extends IService<Repair> {

    Result<Void> applyRepair(Repair repair);

    Result<PageResult<Repair>> getRepair(Integer currentPage, Integer pageSize, Integer userId);

    Result<PageResult<Repair>> getAllRepair(Integer currentPage, Integer pageSize);

    Result<Void> updateRepair(Repair repair);
}