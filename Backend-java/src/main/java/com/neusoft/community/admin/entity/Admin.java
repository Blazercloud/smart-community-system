package com.neusoft.community.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("admin")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username; // 登录账号
    private String password; // 登录密码，存储加密后的
    private String role;     // 角色标识，例如 "admin"
    private String status;

    public int getStatus() {
        return Integer.parseInt(status);
    }

    public Object getName() {
        return username;
    }
}