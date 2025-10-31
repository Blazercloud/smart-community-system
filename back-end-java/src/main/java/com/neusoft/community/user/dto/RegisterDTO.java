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
    
    private String username;
    
    private String email;

    private String roomNumber;

    private String houseType;

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }
}

