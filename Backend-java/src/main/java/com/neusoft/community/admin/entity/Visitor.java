package com.neusoft.community.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("visitor")
public class Visitor {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id; // 访客ID

    private String name; // 访客姓名，可为空

    private String visitorType; // 访客类型: friend/delivery/fix/other，可为空

    private String purpose; // 来访目的，可为空

    private LocalDateTime visitTime; // 到访时间，可为空

    private Integer relatedUser; // 关联业主ID，可为空

    private Integer status; // 状态: 0未到访/1已到访/2已离开，不可为空
}
