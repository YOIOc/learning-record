package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload") /* MultipartFile对象是Spring框架提供，用于封装前端提交文件的对象 */
    public Result<String> Upload(MultipartFile file) throws Exception {
        /* transferTo方法：将上传的文件保存到指定位置
         * getOriginalFilename方法：获取上传文件的原始文件名 */
        // 使用UUID保证文件名唯一，以防止文件存储时发生文件替换
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        // 把文件存储到本地
        String url = AliOssUtil.uploadFile(filename, file.getInputStream());
        return Result.success(url);
    }
}
