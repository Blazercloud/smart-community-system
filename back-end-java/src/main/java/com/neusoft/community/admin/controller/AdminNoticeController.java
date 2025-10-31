package com.neusoft.community.admin.controller;

import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.common.annotation.NoAuth;
import com.neusoft.community.user.entity.Notice;
import com.neusoft.community.user.service.NoticeService;
import com.neusoft.community.user.vo.NoticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/admin/property/notices")
@RestController
public class AdminNoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 更新公告
     */
    @NoAuth
    @PutMapping
    public Result<Void> UpdateNotice(@RequestBody Notice notice) {

        return noticeService.updateNotice(notice);
    }
}
