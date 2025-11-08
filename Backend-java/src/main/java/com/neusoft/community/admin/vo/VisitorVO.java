package com.neusoft.community.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitorVO {

    private Integer id;            // 访客ID
    private String name;           // 访客姓名
    private String visitorType;    // 类型
    private String purpose;        // 来访目的
    private LocalDateTime visitTime; // 到访时间
    private Integer status;        // 状态
    private String relatedUserName; // 关联业主显示: username-room_number
}
