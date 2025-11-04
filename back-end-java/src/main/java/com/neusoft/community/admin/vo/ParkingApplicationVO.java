package com.neusoft.community.admin.vo;

import com.neusoft.community.user.entity.ParkingApplication;

public class ParkingApplicationVO extends ParkingApplication {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}