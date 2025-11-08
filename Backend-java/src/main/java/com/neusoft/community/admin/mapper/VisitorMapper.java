package com.neusoft.community.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.community.admin.entity.Visitor;
import com.neusoft.community.admin.vo.VisitorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VisitorMapper extends BaseMapper<Visitor> {

    List<VisitorVO> selectVisitorVOList(@Param("offset") int offset,
                                        @Param("pageSize") int pageSize,
                                        @Param("keyword") String keyword,
                                        @Param("type") String type);

    Long countVisitorVOList(@Param("keyword") String keyword,
                            @Param("type") String type);
}
