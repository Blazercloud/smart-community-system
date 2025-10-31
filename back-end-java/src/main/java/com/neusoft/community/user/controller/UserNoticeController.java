package com.neusoft.community.user.controller;


import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.common.annotation.NoAuth;
import com.neusoft.community.user.vo.NoticeVO;
import com.neusoft.community.user.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/community")
@Tag(name = "公告管理")
public class UserNoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 分页查询公告列表（关联role表）
     */
    @NoAuth
    @GetMapping("/noticelist")
    @Operation(summary = "分页查询公告列表")
public Result<PageResult<List<NoticeVO>>> getUserNoticeList(
        @RequestParam(defaultValue = "1") Integer currentPage,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false) String searchKeyword,
        @RequestParam(required = false) Integer filterStatus) {

    return this.noticeService.getNoticeList(currentPage, pageSize, searchKeyword, filterStatus);
}

}
