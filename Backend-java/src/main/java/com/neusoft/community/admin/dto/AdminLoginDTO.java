package com.neusoft.community.admin.dto;

import lombok.Data;

/**
 * 管理员登录DTO
 * 
 * @author Neusoft
 */
public class AdminLoginDTO {
    
    private String username;
    
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

