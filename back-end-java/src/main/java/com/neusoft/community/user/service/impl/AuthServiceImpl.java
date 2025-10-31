package com.neusoft.community.user.service.impl;

import com.neusoft.community.common.util.TokenService;
import com.neusoft.community.user.dto.LoginRequest;
import com.neusoft.community.user.dto.LoginResponse;
import com.neusoft.community.user.entity.User;
import com.neusoft.community.user.mapper.UserMapper;
import com.neusoft.community.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TokenService tokenService;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByPhone(request.getPhone());
        if (user == null || !BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("手机号或密码错误");
        }

        if (user.getStatus() == 0) {
            throw new RuntimeException("账户已被禁用");
        }

        // 生成 Redis token
        String token = tokenService.generateTokenForUser(Long.valueOf(user.getPhone()));

        return new LoginResponse(token, user);
    }
}