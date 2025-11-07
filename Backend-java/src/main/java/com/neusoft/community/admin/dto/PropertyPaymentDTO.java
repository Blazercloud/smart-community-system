package com.neusoft.community.admin.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 物业缴费传输对象
 */
@Data
public class PropertyPaymentDTO {
    private Integer id;
    private Integer userId;
    private BigDecimal amount;
    private String type;
    private String status;
    private String payMethod;
    private String note;
}
