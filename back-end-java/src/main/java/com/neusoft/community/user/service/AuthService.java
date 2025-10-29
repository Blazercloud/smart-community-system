package com.neusoft.community.user.service;

import com.neusoft.community.user.dto.LoginRequest;
import com.neusoft.community.user.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}