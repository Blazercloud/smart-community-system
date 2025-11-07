package com.neusoft.community.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物业缴费订单表
 */
@Data
@TableName("property_payment")
public class PropertyPayment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private BigDecimal amount;
    private String type;
    private String status;
    private String payMethod;
    private String note;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
