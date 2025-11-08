package com.neusoft.community.admin.service;

import com.neusoft.community.admin.vo.DashboardOverviewVO;
import com.neusoft.community.common.Result;

public interface AdminDashboardService {

    /**
     * 首页汇总信息
     */
    Result<DashboardOverviewVO> getDashboardOverview();


    /**
     * 最新公告（最近5条）
     */
    Result<?> getLatestNotices();
}
