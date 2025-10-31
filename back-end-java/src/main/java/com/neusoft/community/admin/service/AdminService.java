package com.neusoft.community.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.community.admin.dto.AdminLoginDTO;
import com.neusoft.community.admin.entity.Admin;
import com.neusoft.community.admin.mapper.AdminMapper;
import com.neusoft.community.common.exception.BusinessException;
import com.neusoft.community.common.util.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员服务类
 * 
 * @author Neusoft
 */
@Service
public class AdminService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * 管理员登录
     */
    public Map<String, Object> login(AdminLoginDTO loginDTO) {
        log.info("管理员登录：{}", loginDTO.getUsername());

        // 查询管理员
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, loginDTO.getUsername());
        Admin admin = adminMapper.selectOne(wrapper);

        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }

        // 验证密码
        if (!admin.getPassword().equals(loginDTO.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 检查状态
        if (admin.getStatus() == 0) {
            throw new BusinessException("账户已被禁用");
        }


        // 生成 Redis token（不透明）
        String token = tokenService.generateTokenForUser(admin.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("adminId", admin.getId());
        result.put("token", token);
        result.put("username", admin.getUsername());
        result.put("name", admin.getName());

        return result;
    }

    /**
     * 获取管理员信息
     */
    public Admin getAdminById(Long adminId) {
        return adminMapper.selectById(adminId);
    }
}

