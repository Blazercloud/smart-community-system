package com.neusoft.community.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName parking_application
 */
@TableName(value ="parking_application")
@Data
public class ParkingApplication {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 车位号
     */
    private String spaceNumber;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 0-待审批，1-已同意，2-已退回
     */
    private String status;

    /**
     * 
     */
    private Date applyTime;
}