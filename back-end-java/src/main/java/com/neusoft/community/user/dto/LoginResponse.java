package com.neusoft.community.user.dto;

import lombok.Data;
import com.neusoft.community.user.entity.User;

@Data
public class LoginResponse {
    private String token;
    private Long userId;
    private String phone;
    private String name;
    private String avatar;
    private Integer gender;
    private String address;
    private Integer status;
    
    public LoginResponse(String token, User user) {
        this.token = token;
        this.userId = user.getId();
        this.phone = user.getPhone();
        this.name = user.getName();
        this.avatar = user.getAvatar();
        this.gender = user.getGender();
        this.address = user.getAddress();
        this.status = user.getStatus();
    }
}