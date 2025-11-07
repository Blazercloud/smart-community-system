package com.neusoft.community.admin.service;

import com.neusoft.community.admin.dto.PropertyPaymentDTO;
import com.neusoft.community.admin.vo.PropertyPaymentVO;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 物业缴费管理服务接口
 */
public interface PropertyPaymentService {

    /**
     * 分页查询物业缴费订单
     */
    Result<PageResult<PropertyPaymentVO>> getAllPropertyPayments(Integer currentPage,
                                                                       Integer pageSize,
                                                                       String type,
                                                                       String status);

    /**
     * 新增缴费订单
     */
    Result<Void> addPropertyPayment(PropertyPaymentDTO dto);

    /**
     * 更新缴费订单
     */
    Result<Void> updatePropertyPayment(PropertyPaymentDTO dto);

    /**
     * 删除缴费订单
     */
    Result<Void> deletePropertyPayment(Integer id);

    /**
     * 导出Excel账单
     */
    void exportPropertyPayments(HttpServletResponse response, String type, String status) throws IOException;
}