package com.neusoft.community.common.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 通用 Excel 导出工具类
 * 适用于 List<VO> / List<Entity>
 */
public class ExcelExportUtil {

    /**
     * 导出数据到 Excel 文件
     * @param dataList 导出的数据列表
     * @param fileName 文件名（不含扩展名）
     * @param <T> 数据类型
     * @return ResponseEntity<byte[]> 供 Spring Controller 直接返回
     */
    public static <T> ResponseEntity<byte[]> exportExcel(List<T> dataList, String fileName) {
        if (dataList == null || dataList.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");

            // 创建表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // 写入表头
            T firstObj = dataList.get(0);
            Field[] fields = firstObj.getClass().getDeclaredFields();

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < fields.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(fields[i].getName());
                cell.setCellStyle(headerStyle);
            }

            // 写入数据
            for (int rowIdx = 0; rowIdx < dataList.size(); rowIdx++) {
                Row row = sheet.createRow(rowIdx + 1);
                T obj = dataList.get(rowIdx);
                for (int colIdx = 0; colIdx < fields.length; colIdx++) {
                    fields[colIdx].setAccessible(true);
                    Object value = fields[colIdx].get(obj);
                    row.createCell(colIdx).setCellValue(value == null ? "" : value.toString());
                }
            }

            // 自适应列宽
            for (int i = 0; i < fields.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();

            // 设置响应头
            String encodedFileName = URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
