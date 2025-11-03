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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Integer filterStatus,
        // 新增排序参数
        String sortField,
        String sortOrder) {

    // 1. 参数校验
    if (currentPage == null || currentPage < 1) {
        currentPage = 1;
    }
    if (pageSize == null || pageSize < 1 || pageSize > 100) {
        pageSize = 10;
    }
    //设置分页参数
    Page<Notice> page = new Page<>(currentPage, pageSize);

    QueryWrapper<Notice> wrapper = new QueryWrapper<>();
// 关键：仅当 searchKeyword 非空时，才添加模糊查询条件（OR 关系 + 括号包裹）
    if (StringUtils.isNotBlank(searchKeyword)) {
        wrapper.and(qw -> qw.like("n.content", searchKeyword).or().like("n.title", searchKeyword));
    }
// 拼接 status 条件（默认 AND 关系，空值时自动忽略）
    wrapper.eq(filterStatus != null, "n.status", filterStatus);


    // 4. 新增：处理排序逻辑
    // 映射前端字段到数据库字段（避免直接使用前端传递的字段名，防止SQL注入）
    Map<String, String> fieldMap = new HashMap<>();
    fieldMap.put("createTime", "n.create_time");  // 发布时间（数据库字段）
    fieldMap.put("updateTime", "n.update_time");  // 更新时间（数据库字段）

    // 默认排序字段：updateTime
    String dbField = fieldMap.getOrDefault(sortField, "n.update_time");
    // 默认排序方向：desc（降序，最新的在前）
    boolean isAsc = "asc".equalsIgnoreCase(sortOrder);

    // 添加排序条件
    if (isAsc) {
        wrapper.orderByAsc(dbField);
    } else {
        wrapper.orderByDesc(dbField);
    }

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
        return this.updateById(notice)? Result.success("更新成功") : Result.fail("更新失败");
    }

    @Override
    public Result<Void> createNotice(Notice notice) {
        return this.save( notice)? Result.success("创建成功") : Result.fail("创建失败");
    }

    @Override
    public Result<Void> deleteNotice(Integer id) {
        return this.removeById(id)? Result.success("删除成功") : Result.fail("删除失败");
    }
}




