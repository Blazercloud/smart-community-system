package com.neusoft.community.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.community.admin.dto.ParkingSpaceDTO;
import com.neusoft.community.admin.entity.ParkingSpace;
import com.neusoft.community.admin.service.ParkingSpaceService;
import com.neusoft.community.admin.mapper.ParkingSpaceMapper;
import com.neusoft.community.admin.vo.ParkingSpaceVO;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.user.entity.User;
import com.neusoft.community.user.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
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
    public Result<PageResult<ParkingSpaceVO>> getParkingInfo(Integer currentPage, Integer pageSize, Long Id, String carNumber) {
        // 创建分页对象
        Page<ParkingSpace> page = new Page<>(currentPage, pageSize);
        QueryWrapper<ParkingSpace> wrapper = new QueryWrapper<>();

        // 添加ownerId查询条件（仅当ownerId不为null时）
        if (Id != null) {
            wrapper.eq("owner_id", Id);
        }

        // 添加车牌号模糊查询条件（仅当carNumber不为null时）
        if (carNumber != null && !carNumber.isEmpty()) {
            wrapper.like("car_number", carNumber);
        }
        
        // 执行分页查询
        Page<ParkingSpace> result = this.page(page, wrapper);
        
        // 转换为VO对象并设置用户名
        List<ParkingSpaceVO> voList = result.getRecords().stream().map(parkingSpace -> {
            ParkingSpaceVO vo = new ParkingSpaceVO();
            // 拷贝相同属性
            BeanUtils.copyProperties(parkingSpace, vo);

            // 查询用户信息并设置用户名
                User user = userMapper.selectById(parkingSpace.getOwnerId());
                if (user != null) {
                    vo.setUserName(user.getUsername());
                }
            return vo;
        }).collect(Collectors.toList());
        
        // 封装分页结果
        PageResult<ParkingSpaceVO> pageResult = new PageResult<>();
        pageResult.setTotal(result.getTotal());
        pageResult.setRows(voList);
        
        return Result.success(pageResult);
    }

    @Override
    public Result<String> addParkingSpace(ParkingSpaceDTO parkingSpaceDTO) {
        String userName = parkingSpaceDTO.getUserName();
        User user = userMapper.findByUsername(userName);
        if (user == null) {
            return Result.fail("没有该车主");
        }

        ParkingSpace parkingSpace = new ParkingSpace();
        BeanUtils.copyProperties(parkingSpaceDTO, parkingSpace);
        return this.save(parkingSpace)? Result.success("添加成功") : Result.fail("添加失败");
    }

    @Override
    public Result<Void> updateParkingSpace(ParkingSpaceDTO parkingSpaceDTO) {

        String userName = parkingSpaceDTO.getUserName();
        User user = userMapper.findByUsername(userName);
        if (user == null) {
            return Result.fail("没有该车主");
        }
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setOwnerId(user.getId());
        BeanUtils.copyProperties(parkingSpaceDTO, parkingSpace);
        return this.updateById(parkingSpace)? Result.success("更新成功") : Result.fail("更新失败");
    }
}