package com.neusoft.community.admin.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardOverviewVO {
    private Integer pendingRepairs;
    private Integer pendingComplaints;
    private Integer pendingVisitors;
}
