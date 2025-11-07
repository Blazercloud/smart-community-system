package com.neusoft.community.admin.controller;

import com.neusoft.community.common.Result;
import com.neusoft.community.common.annotation.NoAuth;
import com.neusoft.community.admin.dto.AdminLoginDTO;
import com.neusoft.community.admin.entity.Admin;
import com.neusoft.community.admin.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理员控制器
 * 
 * @author Neusoft
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    /**
     * 管理员登录
     */
    @NoAuth
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody AdminLoginDTO loginDTO) {
        log.info("管理员登录请求：{}", loginDTO.getUsername());
        Map<String, Object> result = adminService.login(loginDTO);
        return Result.success(result);
    }

    /**
     * 获取管理员信息
     */
    @GetMapping("/info")
    public Result<Admin> getAdminInfo(@RequestAttribute("token") String token) {
        // 从token中获取管理员ID（简化处理）
        Long adminId = 1L;
        Admin admin = adminService.getAdminById(adminId);
        return Result.success(admin);
    }
}

