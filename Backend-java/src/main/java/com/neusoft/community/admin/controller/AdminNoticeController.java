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
    @PutMapping
    public Result<Void> UpdateNotice(@RequestBody Notice notice) {

        return noticeService.updateNotice(notice);
    }

    /**
     * 新建公告
     */
    @PostMapping
    public Result<Void> CreateNotice(@RequestBody Notice notice) {

        return noticeService.createNotice(notice);
    }

    /**
     * 删除公告
     */
    @DeleteMapping("/{id}")
    public Result<Void> DeleteNotice(@PathVariable("id") Integer id) {
        return noticeService.deleteNotice(id);
    }
}
