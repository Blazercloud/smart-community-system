package com.neusoft.community.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.community.common.exception.BusinessException;
import com.neusoft.community.common.util.JwtUtil;
import com.neusoft.community.user.dto.LoginDTO;
import com.neusoft.community.user.dto.RegisterDTO;
import com.neusoft.community.user.entity.User;
import com.neusoft.community.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务类
 * 
 * @author Neusoft
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
    public Map<String, Object> register(RegisterDTO registerDTO) {
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
        user.setPassword(BCrypt.hashpw(registerDTO.getPassword(), BCrypt.gensalt())); // 使用 BCrypt 加密
        user.setName(registerDTO.getName());
        user.setAddress(registerDTO.getAddress());
        user.setStatus(1);
        user.setGender(0); // 默认未知
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);

        // 生成 JWT token
        String token = jwtUtil.generateToken(user.getId().toString(), "USER");

        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("token", token);
        result.put("phone", user.getPhone());
        result.put("name", user.getName());
        result.put("address", user.getAddress());

        return result;
    }

    /**
     * 用户登录
     */
    public Map<String, Object> login(LoginDTO loginDTO) {
        log.info("用户登录：{}", loginDTO.getPhone());

        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, loginDTO.getPhone());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证密码
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("账户已被禁用");
        }

        // 生成 JWT token
        String token = jwtUtil.generateToken(user.getId().toString(), "USER");

        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("token", token);
        result.put("phone", user.getPhone());
        result.put("name", user.getName());
        result.put("avatar", user.getAvatar());
        result.put("gender", user.getGender());
        result.put("birthday", user.getBirthday());
        result.put("address", user.getAddress());
        result.put("status", user.getStatus());

        log.info("前端传入明文密码: {}", loginDTO.getPassword());
        log.info("数据库中的加密密码: {}", user.getPassword());
        log.info("匹配结果: {}", BCrypt.checkpw(loginDTO.getPassword(), user.getPassword()));
        log.info("输入密码: '{}'", loginDTO.getPassword());
        log.info("数据库密码: '{}'", user.getPassword());
        log.info("数据库密码长度: {}", user.getPassword().length());
        log.info("匹配结果: {}", BCrypt.checkpw(loginDTO.getPassword(), user.getPassword()));
        return result;

    }

    /**
     * 获取用户信息
     */
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }
}

