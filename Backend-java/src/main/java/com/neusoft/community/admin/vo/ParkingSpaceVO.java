package com.neusoft.community.admin.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data

public class ParkingSpaceVO {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private String spaceNumber;

    /**
     *
     */
    private String status;

    /**
     *
     */
    private String userName;
    /**
     *
     */
    private Integer ownerId;

    /**
     *
     */
    private String carNumber;

}
