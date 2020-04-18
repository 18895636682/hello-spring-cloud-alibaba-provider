package com.zhuzhi.spring.cloud.alibaba.provider.controller;

import com.zhuzhi.spring.cloud.alibaba.provider.common.BaseResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.req.asr.AsrMeta;
import com.zhuzhi.spring.cloud.alibaba.provider.service.asr.AliAsrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/asr")
@Slf4j
@Api(tags = "阿里ASR识别")
public class AliAsr {

    @Autowired
    private AliAsrService aliAsrService;

    @RequestMapping(value = "/aliAsr", method = RequestMethod.POST)
    @ApiOperation("未接原因检测")
    public BaseResponse<String> aliAsr(@RequestBody AsrMeta asrMeta) {
        return aliAsrService.asr(asrMeta);
    }
}
