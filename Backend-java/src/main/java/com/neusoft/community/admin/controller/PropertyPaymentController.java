package com.neusoft.community.admin.controller;

import com.neusoft.community.admin.dto.PropertyPaymentDTO;
import com.neusoft.community.admin.service.PropertyPaymentService;
import com.neusoft.community.admin.vo.PropertyPaymentVO;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 物业缴费管理（管理员端）
 */
@RestController
@RequestMapping("/admin/property/payments")
@Tag(name = "物业缴费管理(管理员端)")
@RequiredArgsConstructor
public class PropertyPaymentController {

    private final PropertyPaymentService propertyPaymentService;

    /**
     * 分页查询缴费订单
     */
    @GetMapping
    @Operation(summary = "分页查询缴费订单")
    public Result<PageResult<PropertyPaymentVO>> getPropertyPayments(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {

        return propertyPaymentService.getAllPropertyPayments(currentPage, pageSize, type, status);
    }

    /**
     * 新增缴费订单
     */
    @PostMapping
    @Operation(summary = "新增缴费订单")
    public Result<Void> addPropertyPayment(@RequestBody PropertyPaymentDTO dto) {
        return propertyPaymentService.addPropertyPayment(dto);
    }

    /**
     * 更新缴费订单
     */
    @PutMapping
    @Operation(summary = "更新缴费订单")
    public Result<Void> updatePropertyPayment(@RequestBody PropertyPaymentDTO dto) {
        return propertyPaymentService.updatePropertyPayment(dto);
    }

    /**
     * 删除缴费订单
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除缴费订单")
    public Result<Void> deletePropertyPayment(@PathVariable Integer id) {
        return propertyPaymentService.deletePropertyPayment(id);
    }

    /**
     * 导出Excel账单
     */
    @GetMapping("/export")
    @Operation(summary = "导出Excel账单")
    public void exportPropertyPayments(HttpServletResponse response,
                                       @RequestParam(required = false) String type,
                                       @RequestParam(required = false) String status) throws IOException {
        propertyPaymentService.exportPropertyPayments(response, type, status);
    }
}