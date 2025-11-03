package com.neusoft.community.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.community.admin.entity.ParkingSpace;
import com.neusoft.community.admin.service.ParkingSpaceService;
import com.neusoft.community.admin.mapper.ParkingSpaceMapper;
import com.neusoft.community.admin.vo.ParkingSpaceVO;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.user.entity.User;
import com.neusoft.community.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author hui
* @description 针对表【parking_space】的数据库操作Service实现
* @createDate 2025-11-02 16:33:12
*/
@Service
public class ParkingSpaceServiceImpl extends ServiceImpl<ParkingSpaceMapper, ParkingSpace> implements ParkingSpaceService{

    @Autowired
    private UserMapper userMapper;
    


    @Override
    public Result<PageResult<List<ParkingSpaceVO>>> getParkingInfo(Integer currentPage, Integer pageSize, Integer id) {
        // 创建分页对象
        Page<ParkingSpace> page = new Page<>(currentPage, pageSize);
        QueryWrapper<ParkingSpace> wrapper = new QueryWrapper<>();
        
        // 添加ownerId查询条件（仅当owernId不为null时）
        if (id != null) {
            wrapper.eq("owner_id", id);
        }
        
        // 执行分页查询
        Page<ParkingSpace> result = this.page(page, wrapper);
        
        // 转换为VO对象并设置用户名
        List<ParkingSpaceVO> voList = result.getRecords().stream().map(parkingSpace -> {
            ParkingSpaceVO vo = new ParkingSpaceVO();
            // 拷贝相同属性
            vo.setId(parkingSpace.getId());
            vo.setSpaceNumber(parkingSpace.getSpaceNumber());
            vo.setStatus(parkingSpace.getStatus());
            vo.setCarNumber(parkingSpace.getCarNumber());
            
            // 查询用户信息并设置用户名
                User user = userMapper.selectById(parkingSpace.getOwnerId());
                if (user != null) {
                    vo.setUserName(user.getUsername());
                }
            return vo;
        }).collect(Collectors.toList());
        
        // 封装分页结果
        PageResult<List<ParkingSpaceVO>> pageResult = new PageResult<>();
        pageResult.setTotal(result.getTotal());
        pageResult.setRows(voList);
        
        return Result.success(pageResult);
    }
}