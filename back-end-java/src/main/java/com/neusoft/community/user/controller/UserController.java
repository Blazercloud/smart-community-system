package com.neusoft.community.user.controller;

import com.neusoft.community.common.Result;
import com.neusoft.community.common.annotation.NoAuth;
import com.neusoft.community.user.dto.LoginDTO;
import com.neusoft.community.user.dto.RegisterDTO;
import com.neusoft.community.user.entity.User;
import com.neusoft.community.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户控制器
 * 
 * @author Neusoft
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @NoAuth
    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody RegisterDTO registerDTO) {
        log.info("用户注册请求：{}", registerDTO.getPhone());
        Map<String, Object> result = userService.register(registerDTO);
        return Result.success(result);
    }

    /**
     * 用户登录
     */
    @NoAuth
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        log.info("用户登录请求：{}", loginDTO.getPhone());
        Map<String, Object> result = userService.login(loginDTO);
        return Result.success(result);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestAttribute("token") String token) {
        // 从token中获取用户ID
        Long userId = Long.parseLong(org.springframework.util.StringUtils.hasText(token) ? token : "1");
        User user = userService.getUserById(userId);
        return Result.success(user);
    }
}

