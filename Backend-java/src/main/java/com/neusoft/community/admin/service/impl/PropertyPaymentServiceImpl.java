package com.neusoft.community.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.community.admin.dto.PropertyPaymentDTO;
import com.neusoft.community.admin.entity.PropertyPayment;
import com.neusoft.community.admin.mapper.PropertyPaymentMapper;
import com.neusoft.community.admin.service.PropertyPaymentService;
import com.neusoft.community.admin.vo.PropertyPaymentVO;
import com.neusoft.community.common.PageResult;
import com.neusoft.community.common.Result;
import com.neusoft.community.user.entity.User;
import com.neusoft.community.user.mapper.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物业缴费服务实现类
 */
@Service
@RequiredArgsConstructor
public class PropertyPaymentServiceImpl implements PropertyPaymentService {

    private final PropertyPaymentMapper propertyPaymentMapper;
    private final UserMapper userMapper;

    /**
     * 分页查询缴费记录
     */
    @Override
    public Result<PageResult<PropertyPaymentVO>> getAllPropertyPayments(Integer currentPage,
                                                                        Integer pageSize,
                                                                        String type,
                                                                        String status) {
        QueryWrapper<PropertyPayment> wrapper = new QueryWrapper<>();

        if (type != null && !type.isEmpty()) {
            wrapper.eq("type", type);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");

        Page<PropertyPayment> page = new Page<>(currentPage, pageSize);
        propertyPaymentMapper.selectPage(page, wrapper);

        // 转换为VO并填充用户名
        List<PropertyPaymentVO> voList = page.getRecords().stream().map(record -> {
            PropertyPaymentVO vo = new PropertyPaymentVO();
            BeanUtils.copyProperties(record, vo);

            User user = userMapper.selectById(record.getUserId());
            if (user != null) {
                vo.setUserName(user.getUsername());
            } else {
                vo.setUserName("未知用户");
            }
            return vo;
        }).collect(Collectors.toList());

        PageResult<PropertyPaymentVO> pageResult = new PageResult<>((int) page.getTotal(), voList);
        return Result.success(pageResult);
    }

    /**
     * 新增缴费记录
     */
    @Override
    public Result<Void> addPropertyPayment(PropertyPaymentDTO dto) {
        PropertyPayment payment = new PropertyPayment();
        BeanUtils.copyProperties(dto, payment);
        propertyPaymentMapper.insert(payment);
        return Result.success();
    }

    /**
     * 更新缴费记录
     */
    @Override
    public Result<Void> updatePropertyPayment(PropertyPaymentDTO dto) {
        PropertyPayment payment = new PropertyPayment();
        BeanUtils.copyProperties(dto, payment);
        propertyPaymentMapper.updateById(payment);
        return Result.success();
    }

    /**
     * 删除缴费记录
     */
    @Override
    public Result<Void> deletePropertyPayment(Integer id) {
        propertyPaymentMapper.deleteById(id);
        return Result.success();
    }

    /**
     * 导出账单为 Excel
     */
    @Override
    public void exportPropertyPayments(HttpServletResponse response, String type, String status) throws IOException {
        QueryWrapper<PropertyPayment> wrapper = new QueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq("type", type);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }

        List<PropertyPayment> list = propertyPaymentMapper.selectList(wrapper);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("物业缴费账单");

        // 表头
        Row header = sheet.createRow(0);
        String[] headers = {"订单ID", "用户名", "缴费金额", "费用类型", "支付状态", "支付方式", "备注", "创建时间"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        // 数据行
        for (int i = 0; i < list.size(); i++) {
            PropertyPayment item = list.get(i);
            Row row = sheet.createRow(i + 1);
            User user = userMapper.selectById(item.getUserId());

            row.createCell(0).setCellValue(item.getId());
            row.createCell(1).setCellValue(user != null ? user.getUsername() : "未知用户");
            row.createCell(2).setCellValue(item.getAmount().doubleValue());
            row.createCell(3).setCellValue(item.getType());
            row.createCell(4).setCellValue(item.getStatus());
            row.createCell(5).setCellValue(item.getPayMethod() == null ? "" : item.getPayMethod());
            row.createCell(6).setCellValue(item.getNote() == null ? "" : item.getNote());
            row.createCell(7).setCellValue(item.getCreateTime() == null ? "" : item.getCreateTime().toString());
        }

        // 响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = URLEncoder.encode("物业缴费账单.xlsx", "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
