package com.neusoft.community.user.controller;

import com.neusoft.community.common.Result;
import com.neusoft.community.common.annotation.NoAuth;
import com.neusoft.community.user.dto.LoginDTO;
import com.neusoft.community.user.dto.LoginResponse;
import jakarta.validation.Valid;
import com.neusoft.community.user.dto.RegisterDTO;
import com.neusoft.community.user.entity.User;
import com.neusoft.community.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/user/register")
    public Result<LoginResponse> register(@RequestBody RegisterDTO registerDTO) {
        log.info("用户注册请求：{}", registerDTO.getPhone());
        LoginResponse result = userService.register(registerDTO);
        return Result.success(result);
    }

    /**
     * 用户登录
     */
    @NoAuth
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("用户登录请求：{}", loginDTO.getPhone());
        LoginResponse result = userService.login(loginDTO);
        return Result.success(result);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/user/info")
    public Result<User> getUserInfo(@RequestAttribute("userId") Long userId) {
        // userId 已由拦截器放入请求属性
        if (userId == null) {
            return Result.fail("Unauthorized");
        }
        User user = userService.getUserById(userId);
        return Result.success(user);
    }
}

