package com.neusoft.community.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.community.admin.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 管理员Mapper
 * 
 * @author Neusoft
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
    @Select("SELECT * FROM admin WHERE username = #{username} LIMIT 1")
    Admin findByUsername(@Param("username") String username);
}


