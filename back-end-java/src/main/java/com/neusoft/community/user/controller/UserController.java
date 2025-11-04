package com.neusoft.community.user.controller;

import com.neusoft.community.common.Result;
import com.neusoft.community.common.annotation.NoAuth;
import com.neusoft.community.user.dto.LoginDTO;
import com.neusoft.community.user.dto.LoginResponse;
import com.neusoft.community.user.dto.RegisterDTO;
import com.neusoft.community.user.entity.User;
import com.neusoft.community.user.service.UserService;
import jakarta.validation.Valid;
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
    @PostMapping("/register")
    public Result<LoginResponse> register(@RequestBody RegisterDTO registerDTO) {
        log.info("【用户注册】手机号: {}", registerDTO.getPhone());
        LoginResponse result = userService.register(registerDTO);
        return Result.success(result);
    }

    /**
     * 用户登录
     */
    @NoAuth
    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("【用户登录】手机号: {}", loginDTO.getPhone());

        LoginResponse result = userService.login(loginDTO);


        // ✅ 直接从 LoginResponse 获取用户ID和用户名
        return Result.success(
                new java.util.HashMap<String, Object>() {{
                    put("token", result.getToken());
                    put("userId", result.getId());
                    put("phone", result.getPhone());
                    put("name", result.getUsername());
                }}
        );
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestAttribute(value = "userId", required = false) Long userId) {
        if (userId == null) {
            return Result.fail("Unauthorized");
        }
        User user = userService.getUserById(userId);
        return Result.success(user);
    }
}
