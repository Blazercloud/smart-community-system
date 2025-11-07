package com.neusoft.community.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.user.entity.Repair;
import com.neusoft.community.user.service.RepairService;
import com.neusoft.community.user.mapper.RepairMapper;
import com.neusoft.community.common.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hui
 * @description 针对表【repair】的数据库操作Service实现
 * @createDate 2025-11-05 12:01:50
 */
@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService{

    /**
     * 新增事务注解，确保数据操作原子性；主动抛出异常，交给全局处理器统一处理
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 捕获所有异常，触发事务回滚
    public Result<Void> applyRepair(Repair repair) {
        // 1. 业务参数校验（示例：校验报修类型不能为空）
        if (repair.getRepairType() == null || repair.getRepairType().isEmpty()) {
            throw new BusinessException("报修类型不能为空");
        }
        // 2. 执行保存操作，失败则抛出异常
        boolean saveSuccess = this.save(repair);
        if (!saveSuccess) {
            throw new BusinessException("报修申请失败");
        }
        // 3. 后续若扩展业务（如同步数据、发消息），异常会触发回滚
        // 示例：syncRepairStat(repair.getUserId()); // 假设的扩展方法
        return Result.success("报修申请成功");
    }

    @Override
    public Result<PageResult<Repair>> getRepair(Integer currentPage, Integer pageSize, Integer userId) {
        // 1. 参数校验，不合法则抛出业务异常
        if (userId == null) {
            throw new BusinessException("非法查询：用户ID不能为空");
        }
        if (currentPage == null || currentPage <= 0 || pageSize == null || pageSize <= 0) {
            throw new BusinessException("分页参数不合法：页码和每页条数必须为正整数");
        }

        // 2. 构建分页对象和查询条件（去掉冗余的userId判断）
        Page<Repair> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Repair> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("create_time");

        // 3. 执行分页查询
        Page<Repair> repairPage = this.page(page, queryWrapper);
        List<Repair> repairs = repairPage.getRecords();

        // 4. 封装分页结果（无数据时返回空列表，而非失败）
        PageResult<Repair> pageResult = new PageResult<>();
        pageResult.setTotal(repairPage.getTotal());
        pageResult.setRows(repairs);

        if (repairPage.getRecords().isEmpty()) {
            return Result.success("暂无报修记录");
        }
        return Result.success(pageResult);
    }

    @Override
    public Result<PageResult<Repair>> getAllRepair(Integer currentPage, Integer pageSize) {

        //默认的分页参数
        if (currentPage == null || currentPage < 1) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        // 1. 构建分页对象和查询条件
        Page<Repair> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Repair> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        // 2. 执行分页查询
        Page<Repair> repairPage = this.page(page, queryWrapper);
        List<Repair> repairs = repairPage.getRecords();

        // 3. 封装分页结果（无数据时返回空列表，而非失败）
        PageResult<Repair> pageResult = new PageResult<>();
        pageResult.setTotal(repairPage.getTotal());
        pageResult.setRows(repairs);
        if (repairPage.getRecords().isEmpty()) {
            return Result.success("暂无报修记录");
        }

        return  Result.success(pageResult);
    }

    @Override
    public Result<Void> updateRepair(Repair repair) {
        return this.updateById(repair) ? Result.success("修改成功") : Result.fail("修改失败");
    }
}