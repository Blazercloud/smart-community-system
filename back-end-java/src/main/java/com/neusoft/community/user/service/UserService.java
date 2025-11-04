package com.neusoft.community.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.community.common.exception.BusinessException;
import com.neusoft.community.common.util.JwtUtil;
import com.neusoft.community.user.dto.LoginDTO;
import com.neusoft.community.user.dto.LoginResponse;
import com.neusoft.community.user.dto.RegisterDTO;
import com.neusoft.community.user.entity.User;
import com.neusoft.community.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务类
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户注册
     */
    @Transactional
    public LoginResponse register(RegisterDTO registerDTO) {
        log.info("用户注册：{}", registerDTO.getPhone());

        // 检查手机号是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, registerDTO.getPhone());
        User existingUser = userMapper.selectOne(wrapper);
        if (existingUser != null) {
            throw new BusinessException("该手机号已被注册");
        }

        // 创建新用户
        User user = new User();
        user.setPhone(registerDTO.getPhone());
        user.setPassword(BCrypt.hashpw(registerDTO.getPassword(), BCrypt.gensalt()));
        user.setUsername(registerDTO.getUsername());
        user.setRoomNumber(registerDTO.getRoomNumber());
        user.setHouseType(registerDTO.getHouseType());
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());

        // 生成 JWT token

        userMapper.insert(user);
        String token = jwtUtil.generateToken(user.getPhone().toString(), "USER");

        return new LoginResponse(token, user);
    }

    /**
     * 用户登录
     */
    public LoginResponse login(LoginDTO loginDTO) {
        log.info("用户登录：{}", loginDTO.getPhone());

        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, loginDTO.getPhone());
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证密码（假设使用 BCrypt 加密）
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

//        //验证密码明文，后期需要加密
//        if (!loginDTO.getPassword().equals(user.getPassword())) {
//            throw new BusinessException("密码错误");
//        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("账户已被禁用");
        }

        // 生成 JWT token
        String token = jwtUtil.generateToken(user.getPhone().toString(), "USER");

        return new LoginResponse(token, user);
    }

    /**
     * 获取用户信息
     */
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }
}
