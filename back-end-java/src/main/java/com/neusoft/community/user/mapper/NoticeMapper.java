package com.neusoft.community.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.community.user.vo.NoticeVO;
import com.neusoft.community.user.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.management.Query;

/**
* @author hui
* @description 针对表【notice(公告表)】的数据库操作Mapper
* @createDate 2025-10-29 17:35:58
* @Entity com.neusoft.community.user.entity.Notice
*/
public interface NoticeMapper extends BaseMapper<Notice> {

    // 分页查询公告VO（关联admin表，使用注解SQL）
    @Select("SELECT n.*, a.role AS publisherName " +
            "FROM notice n " +
            "LEFT JOIN admin a ON n.publisher_id = a.id "+
            "${ew.customSqlSegment}"
    )
    IPage<NoticeVO> selectNoticeVOPage(
            Page<NoticeVO> page,
            @Param(Constants.WRAPPER) QueryWrapper<Notice> queryWrapper
    );

}




