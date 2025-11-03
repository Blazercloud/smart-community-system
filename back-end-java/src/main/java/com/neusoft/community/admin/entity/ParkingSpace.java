package com.neusoft.community.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName parking_space
 */
@TableName(value ="parking_space")
@Data
public class ParkingSpace {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String spaceNumber;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private Integer ownerId;

    /**
     * 
     */
    private String carNumber;
}