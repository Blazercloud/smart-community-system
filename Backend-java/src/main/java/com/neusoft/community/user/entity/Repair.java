package com.neusoft.community.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.*;
import java.util.Date;

import lombok.Data;

/**
 *
 * @TableName repair
 */
@TableName(value = "repair")
@Data
public class Repair {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    /**
     * 报修类型
     */
    @NotBlank(message = "报修类型不能为空")
    @Size(max = 50, message = "报修类型长度不能超过50字符")
    private String repairType;

    /**
     * 报修标题
     */
    @NotBlank(message = "报修标题不能为空")
    @Size(max = 100, message = "报修标题长度不能超过100字符")
    private String title;

    /**
     * 详细描述
     */
    @NotBlank(message = "详细描述不能为空")
    @Size(max = 1000, message = "详细描述长度不能超过1000字符")
    private String description;

    /**
     * 图片URL
     */
//    @Pattern(regexp = "^https?://.*\\.(jpg|jpeg|png|gif)$", message = "图片URL格式不正确")
    @Size(max = 1000, message = "图片URL长度不能超过1000字符")
    private String imgUrl;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入有效的手机号")
    private String phone;

    /**
     * 期望维修时间
     */
    @NotNull(message = "期望维修时间不能为空")
    private Date expectedTime;

    /**
     * 报修状态：0-未审核，1-已审核，2-已处理
     */
    @Min(value = 0, message = "状态异常")
    @Max(value = 2, message = "状态异常")
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
     * 指派维修人员
     */
    @Size(max = 50, message = "维修人员名称长度不能超过50字符")
    private String assignedWorker;

    /**
     * 处理详情
     */
    @Size(max = 500, message = "处理详情长度不能超过500字符")
    private String handleDetail;
}
