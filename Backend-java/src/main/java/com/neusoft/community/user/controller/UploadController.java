package com.neusoft.community.user.controller;

import com.neusoft.community.common.util.MinioUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@Tag(name = "文件上传接口")
public class UploadController {
    @Autowired
    private MinioUtil minioUtil;

    /**
     * 上传文件
     */
    @PostMapping(value = "/upload")
    @Operation(summary = "上传文件")
    public String uploadReport(MultipartFile file) {
        // 获取到上传的文件名
        String fileName = file.getOriginalFilename();
        // 上传文件
        minioUtil.upload(file, fileName);
        // 获取上传的文件地址
        return minioUtil.getFileUrl(fileName);
    }

    /**
     * 预览文件
     */
    @GetMapping("/preview")
    @Operation(summary = "预览文件")
    public String preview(String fileName) {
        return minioUtil.getFileUrl(fileName);
    }

    /**
     * 下载文件
     */
    @GetMapping("/download")
    @Operation(summary = "下载文件")
    public void download(String fileName, HttpServletResponse response) {
        minioUtil.download(response, fileName);
    }

    /**
     * 删除文件
     */
    @GetMapping("/delete")
    @Operation(summary = "删除文件")
    public String delete(String fileName) {
        minioUtil.delete(fileName);
        return "删除成功";
    }
}
