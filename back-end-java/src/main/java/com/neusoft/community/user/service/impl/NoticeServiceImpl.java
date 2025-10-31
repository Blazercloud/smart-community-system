package com.neusoft.community.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.user.vo.NoticeVO;
import com.neusoft.community.user.entity.Notice;
import com.neusoft.community.user.service.NoticeService;
import com.neusoft.community.user.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author hui
* @description 针对表【notice(公告表)】的数据库操作Service实现
* @createDate 2025-10-29 17:35:58
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService{


    @Autowired
    private NoticeMapper noticeMapper;
@Override
public Result<PageResult<List<NoticeVO>>> getNoticeList(
        Integer currentPage,
        Integer pageSize,
        String searchKeyword,
        Integer filterStatus) {

    // 1. 参数校验
    if (currentPage == null || currentPage < 1) {
        currentPage = 1;
    }
    if (pageSize == null || pageSize < 1 || pageSize > 100) {
        pageSize = 10;
    }
    //设置分页参数
    Page<NoticeVO> page = new Page<>(currentPage, pageSize);



    QueryWrapper<Notice> wrapper = new QueryWrapper<>();
    wrapper.like(StringUtils.isNotBlank(searchKeyword), "n.content", searchKeyword)
            .or()
            .like(StringUtils.isNotBlank(searchKeyword), "n.title", searchKeyword)
            .eq(filterStatus != null, "n.status", filterStatus);
    // 2. 再执行查询（传入构建好的条件）
    IPage<NoticeVO> noticeVOPage = noticeMapper.selectNoticeVOPage(page, wrapper);  // 这里传入queryWrapper


    // 3. 添加条件查询逻辑（使用自定义 SQL 或 Mapper 新方法）
    // 注意：当前 selectNoticeVOPage 没有支持条件，需修改 Mapper 层
    List<NoticeVO> noticeList = noticeVOPage.getRecords();

    // 4. 逻辑判断：无数据时返回提示
    if (noticeList == null || noticeList.isEmpty()) {
        return Result.fail("当前暂无公告数据");
    }

    // 5. 封装分页结果
    PageResult<List<NoticeVO>> pageResult = new PageResult<>();
    pageResult.setTotal(noticeVOPage.getTotal());
    pageResult.setRows(noticeList);

    // 6. 返回成功结果
    return Result.success(pageResult);
}


    @Override
    public Result<Void> updateNotice(Notice notice) {
        return this.updateById( notice)? Result.success("更新成功") : Result.fail("更新失败");
    }
}




