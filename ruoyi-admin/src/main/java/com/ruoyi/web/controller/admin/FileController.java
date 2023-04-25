package com.ruoyi.web.controller.admin;

import com.asugar.ftwaterdelivery.utils.CommonUtils;
import com.asugar.ftwaterdelivery.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/31 18:46
 */
@Api(tags = "文件管理")
@RestController
@RequestMapping("/file")
public class FileController {

    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) {
        return Result.success(CommonUtils.uploadFile(file, "default"));
    }
}
