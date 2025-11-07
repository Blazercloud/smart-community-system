package com.neusoft.community.user.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class NoticeVO {
    /**
     * 公告ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 类型：1-社区公告，2-物业通知
     */
    private Integer type;

    /**
     * 发布人ID
     */
    private Long publisherId;

    /**
     * 状态：0-草稿，1-已发布
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 来自role表的发布人名称
     */
    private String publisherName;
}
