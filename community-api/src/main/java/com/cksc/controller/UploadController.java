package com.cksc.controller;

import com.cksc.common.aop.LogAnnotation;
import com.cksc.utils.QiniuUtils;
import com.cksc.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID; //ID生成工具

/**
 * 图片上传控制类
 */

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private QiniuUtils qiniuUtils;

    /**
     * 接收图片上传 用spring专门接收文件参数的MultipartFile
     * spring 默认支持上传1M 但可配置上传大小
     * @param file
     * @return
     */
    @PostMapping
    @ApiOperation(value="接收图片上传 ",notes = "根据文件参数")
    //@LogAnnotation(module="接收图片上传",operator="根据文件参数")
    public Result upload(@RequestParam("image") MultipartFile file){
        //原始文件名称 比如 aa.png
        String originalFilename = file.getOriginalFilename();
        //唯一的文件名称 字符串连接 用点做切分拿到图片后缀名
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
        //上传文件 上传到哪呢？ 七牛云 云服务器 按量付费 速度快 把图片发放到离用户最近的服务器上
        // 降低 我们自身应用服务器的带宽消耗

        //传入MultipartFile、文件名称
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            //返回上传成功的图片文件路径
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }
}
