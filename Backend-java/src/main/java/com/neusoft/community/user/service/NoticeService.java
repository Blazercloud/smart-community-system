package com.neusoft.community.user.service;

import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.user.vo.NoticeVO;
import com.neusoft.community.user.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author hui
* @description 针对表【notice(公告表)】的数据库操作Service
* @createDate 2025-10-29 17:35:58
*/
public interface NoticeService extends IService<Notice> {
  Result <PageResult<NoticeVO>> getNoticeList(Integer currentPage, Integer pageSize, String searchKeyword, Integer filterStatus,// 新增排序参数
            String sortField, String sortOrder);

    Result<Void> updateNotice(Notice notice);

    Result<Void> createNotice(Notice notice);

  Result<Void> deleteNotice(Integer id);
}