package com.zhuzhi.spring.cloud.alibaba.provider.controller;

import com.zhuzhi.spring.cloud.alibaba.provider.common.BaseResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.shared.RecordFileShared;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RefreshScope
@RestController
@RequestMapping(value = "/api/upload")
@Slf4j
@Api(tags = "文件上传")
public class UploadController {

    @Value("${zhuzhi.filNamePre}")
    private String filNamePre;

    @Value("${zhuzhi.recordFilNamePre}")
    private String recordFilNamePre;

    @Value("${zhuzhi.vadDirPre}")
    private String vadDirPre;

    @Autowired
    @Qualifier("redisson2")
    private RedissonClient redissonClient;

    @RequestMapping(value = "/uploadRecordFiles", method = RequestMethod.POST)
    @ApiOperation("表单多文件上传")
    public BaseResponse<?> uploadRecordFiles(@RequestParam("file[]") MultipartFile[] recordFiles, HttpServletRequest request) throws IOException {
        if (recordFiles != null && recordFiles.length > 0) {
            for (int i = 0; i < recordFiles.length; i++) {
                MultipartFile fileOne = recordFiles[i];
                log.info(fileOne.getOriginalFilename());
                log.info(fileOne.getContentType());

                String fileName = request.getParameter("fileName" + i);
                if (StringUtils.isNotBlank(fileName)) {
                    File file = new File(filNamePre + fileName); //如果文件夹不存在则创建
                    if (!file.exists() && !file.isDirectory()) {
                        log.info("zhuz: 文件夹不存在:" + file);
                        if (file.mkdirs()) {
                            fileOne.transferTo(new File(filNamePre + fileName + fileOne.getOriginalFilename()));
                        } else {
                            log.error("zhuz: 创建文件夹失败---file:" + file);
                        }
                    } else {
                        fileOne.transferTo(new File(filNamePre + fileName + fileOne.getOriginalFilename()));
                        log.info("zhuz: 文件夹存在:" + file);
                    }

                } else {
                    fileOne.transferTo(new File(filNamePre + fileOne.getOriginalFilename()));
                }
            }
            return BaseResponse.success("操作成功0.0");
        } else {
            return BaseResponse.failed("操作失败~_~");
        }
    }

    @RequestMapping(value = "/uploadRecordFile", method = RequestMethod.POST)
    @ApiOperation("表单单文件上传-全录音")
    public BaseResponse<?> uploadRecordFile(@RequestParam("file") MultipartFile recordFile) throws IOException {
        if (recordFile != null) {

            String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/";
            File file = new File(recordFilNamePre + fileName); //如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                log.info("zhuz: 文件夹不存在---file is" + recordFilNamePre + fileName + recordFile.getOriginalFilename());
                if (file.mkdirs()) {
                    recordFile.transferTo(new File(recordFilNamePre + fileName + recordFile.getOriginalFilename()));
                    //将录音名在redis中存储1分钟 供全录音语音识别调用
                    RBucket<Object> result = redissonClient.getBucket(RecordFileShared.record_file_key + fileName + recordFile.getOriginalFilename());
                    result.set("1", 1L, TimeUnit.MINUTES);
                } else {
                    log.error("zhuz: 创建文件夹失败---file:" + file);
                }
            } else {
                recordFile.transferTo(new File(recordFilNamePre + fileName + recordFile.getOriginalFilename()));
                //将录音名在redis中存储1分钟 供全录音语音识别调用
                RBucket<Object> result = redissonClient.getBucket(RecordFileShared.record_file_key + fileName + recordFile.getOriginalFilename());
                result.set("1", 1L, TimeUnit.MINUTES);
                log.info("zhuz: 文件夹存在---file is " + recordFilNamePre + fileName + recordFile.getOriginalFilename());
            }

            return BaseResponse.success("操作成功0.0");
        } else {
            return BaseResponse.failed("操作失败~_~");
        }
    }

    @RequestMapping(value = "/uploadVadFile", method = RequestMethod.POST)
    @ApiOperation("表单单文件上传-小录音")
    public BaseResponse<?> uploadVadFile(@RequestParam("file") MultipartFile recordFile) throws IOException {
        if (recordFile != null) {

            String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/";
            File file = new File(vadDirPre + fileName); //如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                log.info("zhuz: 文件夹不存在---file is" + file + recordFile.getOriginalFilename());
                if (file.mkdirs()) {
                    recordFile.transferTo(new File(vadDirPre + fileName + recordFile.getOriginalFilename()));
                } else {
                    log.error("zhuz: 创建文件夹失败---file:" + file);
                }
            } else {
                recordFile.transferTo(new File(vadDirPre + fileName + recordFile.getOriginalFilename()));
                log.info("zhuz: 文件夹存在---file is " + file + recordFile.getOriginalFilename());
            }

            return BaseResponse.success("操作成功0.0");
        } else {
            return BaseResponse.failed("操作失败~_~");
        }
    }

}
