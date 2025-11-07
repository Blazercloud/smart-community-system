package com.neusoft.community.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("complaint")
public class Complaint {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;
    private String content;
    private Integer isHandled;
    private String handleResult;
    private LocalDateTime createTime;
}
