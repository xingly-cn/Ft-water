package com.ruoyi.web.controller.admin;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.utils.CommonUtils;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin
public class FileController {

    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile file) {
        return AjaxResult.success(CommonUtils.uploadFile(file, "default"));
    }
}
