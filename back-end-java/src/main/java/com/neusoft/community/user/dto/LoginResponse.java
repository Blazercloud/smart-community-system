package com.neusoft.community.user.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neusoft.community.user.entity.User;

@Data
public class LoginResponse {
    private String token;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("room_number")
    private String roomNumber;

    @JsonProperty("house_type")
    private String houseType;

    @JsonProperty("create_time")
    private String createTime;

    @JsonProperty("status")
    private Integer status;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.id=user.getId();
        this.username = user.getUsername();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.roomNumber = user.getRoomNumber();
        this.houseType = user.getHouseType();

        // ✅ 这里根据 user 中类型决定
        if (user.getCreateTime() != null) {
            this.createTime = user.getCreateTime().toString();
        }
        this.status = user.getStatus();
    }

}
