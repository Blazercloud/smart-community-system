package com.neusoft.community.admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物业缴费返回视图对象（返回给前端）
 */
@Data
public class PropertyPaymentVO {
    private Integer id;
    private Integer userId;
    private String userName; // ✅ 建议增加：方便展示用户名
    private BigDecimal amount;
    private String type;
    private String status;
    private String payMethod;
    private String note;
    private LocalDateTime createTime;
    private LocalDateTime updateTime; // ✅ 建议增加
}
