package com.neusoft.community.admin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data

public class ParkingSpaceDTO {

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
    private String carNumber;

}
