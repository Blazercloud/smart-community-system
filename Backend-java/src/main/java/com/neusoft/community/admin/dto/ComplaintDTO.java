package com.neusoft.community.admin.dto;

import lombok.Data;

@Data
public class ComplaintDTO {
    private Integer id;
    private Integer isHandled;     // 0-未处理，1-已处理
    private String handleResult;   // 处理结果描述
}
