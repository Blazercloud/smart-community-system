package com.neusoft.community.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ComplaintVO {
    private Integer id;
    private Integer userId;
    private String content;
    private Integer isHandled;
    private String handleResult;
    private LocalDateTime createTime;
}
