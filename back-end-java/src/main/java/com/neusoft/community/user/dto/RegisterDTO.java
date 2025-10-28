package com.neusoft.community.user.dto;

import lombok.Data;

/**
 * 注册DTO
 * 
 * @author Neusoft
 */
@Data
public class RegisterDTO {
    
    private String phone;
    
    private String password;
    
    private String name;
    
    private String address;
}

