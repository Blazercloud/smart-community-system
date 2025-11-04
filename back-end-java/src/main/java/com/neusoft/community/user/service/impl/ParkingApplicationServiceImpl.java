package com.neusoft.community.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.community.admin.entity.ParkingSpace;
import com.neusoft.community.admin.mapper.ParkingSpaceMapper;
import com.neusoft.community.admin.vo.ParkingApplicationVO;
import com.neusoft.community.admin.vo.ParkingSpaceVO;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.user.entity.ParkingApplication;
import com.neusoft.community.user.entity.User;
import com.neusoft.community.user.mapper.UserMapper;
import com.neusoft.community.user.service.ParkingApplicationService;
import com.neusoft.community.user.mapper.ParkingApplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author hui
* @description 针对表【parking_application】的数据库操作Service实现
* @createDate 2025-11-03 11:44:42
*/

@Service
public class ParkingApplicationServiceImpl extends ServiceImpl<ParkingApplicationMapper, ParkingApplication>
    implements ParkingApplicationService{


    @Autowired
    private UserMapper userMapper;


    @Autowired
    private ParkingSpaceMapper parkingSpaceMapper;


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
    /**
     * 获取所有停车申请（带用户信息）
     */
    @Override
    public Result<PageResult<List<ParkingApplicationVO>>> getAllParkingApplication(Integer currentPage,Integer pageSize) {
        Page<ParkingApplication> page = new Page<>(currentPage, pageSize);
        QueryWrapper<ParkingApplication> wrapper = new QueryWrapper<>();


        // 执行分页查询
        Page<ParkingApplication> result = this.page(page, wrapper);


        // 转换为VO对象并添加用户信息
        List<ParkingApplicationVO> applicationVOs = result.getRecords().stream().map(application -> {
            ParkingApplicationVO vo = new ParkingApplicationVO();
            vo.setId(application.getId());
            vo.setUserId(application.getUserId());
            vo.setSpaceNumber(application.getSpaceNumber());
            vo.setCarNumber(application.getCarNumber());
            vo.setStatus(application.getStatus());
            vo.setApplyTime(application.getApplyTime());

            // 查询用户信息
            User user = userMapper.selectById(application.getUserId());
            if (user != null) {
                vo.setUsername(user.getUsername());
            }

            return vo;
        }).collect(Collectors.toList());


        PageResult<List<ParkingApplicationVO>> pageResult = new PageResult<>();
        pageResult.setTotal(result.getTotal());
        pageResult.setRows(applicationVOs);


        return Result.success(pageResult);
    }

    @Override
    public Result<Void> updateParkingApplicationStatus(Integer id, ParkingApplication application) {
        ParkingApplication existingApplication = this.getById(id);
        if (existingApplication == null) {
            return Result.fail("申请记录不存在");
        }

        // 更新状态
        existingApplication.setStatus(application.getStatus());
        boolean success = this.updateById(existingApplication);

        // 如果申请被同意，则更新车位信息
        if (success && "1".equals(application.getStatus())) {
            // 根据车位号查找车位
            ParkingSpace parkingSpace = parkingSpaceMapper.selectOne(
                    new QueryWrapper<ParkingSpace>().eq("space_number", existingApplication.getSpaceNumber())
            );

            if (parkingSpace != null) {
                // 更新车位的车主信息
                parkingSpace.setOwnerId(existingApplication.getUserId());
                parkingSpace.setCarNumber(existingApplication.getCarNumber());
                parkingSpace.setStatus("已占用"); // 更新车位状态为已占用
                parkingSpaceMapper.updateById(parkingSpace);
            }
        }

        return success? Result.success("申请状态更新成功") : Result.fail("申请状态更新失败");

    }
}




