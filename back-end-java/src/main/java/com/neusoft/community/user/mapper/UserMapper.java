package com.neusoft.community.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.community.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 * 
 * @author Neusoft
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

