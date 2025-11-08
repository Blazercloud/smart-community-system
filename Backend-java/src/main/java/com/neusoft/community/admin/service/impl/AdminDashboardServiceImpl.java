package com.neusoft.community.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.community.admin.entity.PropertyPayment;
import com.neusoft.community.user.entity.Notice;
import com.neusoft.community.user.entity.Repair;
import com.neusoft.community.admin.entity.Complaint;
import com.neusoft.community.admin.entity.Visitor;
import com.neusoft.community.admin.mapper.PropertyPaymentMapper;
import com.neusoft.community.user.mapper.NoticeMapper;
import com.neusoft.community.user.mapper.RepairMapper;
import com.neusoft.community.admin.mapper.ComplaintMapper;
import com.neusoft.community.admin.mapper.VisitorMapper;
import com.neusoft.community.admin.service.AdminDashboardService;
import com.neusoft.community.admin.vo.DashboardOverviewVO;
import com.neusoft.community.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final PropertyPaymentMapper propertyPaymentMapper;
    private final NoticeMapper noticeMapper;
    private final RepairMapper repairMapper;
    private final ComplaintMapper complaintMapper;
    private final VisitorMapper visitorMapper;

    @Override
    public Result<DashboardOverviewVO> getDashboardOverview() {
        DashboardOverviewVO vo = new DashboardOverviewVO();

        // 统计待处理事项（将 Long 转为 Integer）
        Integer pendingRepairs = repairMapper.selectCount(new QueryWrapper<Repair>().eq("status", "pending")).intValue();
        Integer pendingComplaints = complaintMapper.selectCount(new QueryWrapper<Complaint>().eq("is_handled", "pending")).intValue();
        Integer pendingVisitors = visitorMapper.selectCount(new QueryWrapper<Visitor>().eq("status", "pending")).intValue();

        // 统计本月缴费总额
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1);
        BigDecimal totalThisMonth = propertyPaymentMapper.selectList(
                        new QueryWrapper<PropertyPayment>()
                                .between("create_time", startOfMonth, endOfMonth)
                                .eq("status", "paid")
                ).stream().map(PropertyPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        vo.setPendingRepairs(pendingRepairs);
        vo.setPendingComplaints(pendingComplaints);
        vo.setPendingVisitors(pendingVisitors);
        vo.setTotalPaymentThisMonth(totalThisMonth);

        return Result.success(vo);
    }

    @Override
    public Result<?> getLatestNotices() {
        List<Notice> notices = noticeMapper.selectList(
                new QueryWrapper<Notice>()
                        .orderByDesc("create_time")
                        .last("LIMIT 5")
        );

        List<Map<String, Object>> list = notices.stream().map(n -> {
            Map<String, Object> item = new HashMap<>();
            item.put("title", n.getTitle());
            item.put("content", n.getContent());
            item.put("createTime", n.getCreateTime());
            return item;
        }).collect(Collectors.toList());

        return Result.success(list);
    }
}
