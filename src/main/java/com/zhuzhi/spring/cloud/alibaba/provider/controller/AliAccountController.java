package com.zhuzhi.spring.cloud.alibaba.provider.controller;

import com.zhuzhi.spring.cloud.alibaba.provider.common.BaseResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.common.PageResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.common.aliasr.AliAsrResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.domain.aliaccount.AliAccount;
import com.zhuzhi.spring.cloud.alibaba.provider.req.aliaccount.AliAccountSearch;
import com.zhuzhi.spring.cloud.alibaba.provider.service.aliaccount.AliAccountService;
import com.zhuzhi.spring.cloud.alibaba.provider.shared.AliAccountShared;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/api/aliaccount")
@Slf4j
@Api(tags = "阿里识别账号管理CRUD")
/*@CrossOrigin*/
public class AliAccountController {

    @Autowired
    private AliAccountService aliAccountService;

    @RequestMapping(value = "/addAliAccount", method = RequestMethod.POST)
    @ApiOperation("阿里识别账号增改")
    public BaseResponse<?> addAliAccount(@RequestBody AliAccount aliAccount) {

        int success = -1;
        if (aliAccount.getId() == null) {
            success = aliAccountService.create(aliAccount);
        } else {
            success = aliAccountService.update(aliAccount);
        }

        if (success > 0) {
            return BaseResponse.success("操作成功0.0");
        } else {
            return BaseResponse.failed("操作失败~_~");
        }
    }

    @RequestMapping(value = "/deleteAliAccount", method = RequestMethod.POST)
    @ApiOperation("阿里识别账号删除")
    public BaseResponse<?> deleteAliAccount(@RequestBody AliAccount aliAccount) {

        int success = -1;
        success = aliAccountService.delete(aliAccount);

        if (success > 0) {
            return BaseResponse.success("操作成功0.0");
        } else {
            return BaseResponse.failed("操作失败~_~");
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("阿里识别账号查询")
    public PageResponse<?> listByPage(@RequestBody AliAccountSearch aliAccountSearch) {
        PageInfo<AliAccount> pageInfoAliAccount = aliAccountService.listByPage(aliAccountSearch);
        return PageResponse.successPageData(pageInfoAliAccount.getList(), pageInfoAliAccount.getTotal(), pageInfoAliAccount.getPageNum());
    }

    //阿里账号轮询获取
    @RequestMapping(value = "/getAliAsr", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("aliAsr账号库资源获取")
    public BaseResponse<AliAsrResponse> getAliAsr() {
        return aliAccountService.getAliAsr();
    }
}
