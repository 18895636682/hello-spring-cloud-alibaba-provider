package com.zhuzhi.spring.cloud.alibaba.provider.controller;

import com.zhuzhi.spring.cloud.alibaba.provider.common.BaseResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.common.PageResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.domain.Mobileloc;
import com.zhuzhi.spring.cloud.alibaba.provider.req.mobileloc.MobileLocSearch;
import com.zhuzhi.spring.cloud.alibaba.provider.service.MobilelocService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/mobileloc")
@Slf4j
@Api(tags = "号码段CRUD")
/*@CrossOrigin*/
public class MobilelocController {

    @Autowired
    private MobilelocService mobilelocService;

    @RequestMapping(value = "/selectOne", method = RequestMethod.POST)
    @ApiOperation("根据号码前七位查单个号码段")
    public Mobileloc selectOne(@RequestBody Mobileloc mobileloc) {
        try {
            Mobileloc result = mobilelocService.selectOne(mobileloc);
            if (result != null) {
                return result;
            } else {
                return new Mobileloc();
            }
        } catch (Exception e) {
            log.error("zhuz: error-" + e);
            return new Mobileloc();
        }
    }

    @RequestMapping(value = "/addMobileloc", method = RequestMethod.POST)
    @ApiOperation("号码段增改")
    public BaseResponse<?> addMobileloc(@RequestBody Mobileloc mobileloc) {

        int success = -1;
        if (mobileloc.getId() == null) {
            success = mobilelocService.create(mobileloc);
        } else {
            success = mobilelocService.update(mobileloc);
        }

        if (success > 0) {
            return BaseResponse.success("操作成功0.0");
        } else {
            return BaseResponse.failed("操作失败~_~");
        }
    }

    @RequestMapping(value = "/deleteMobileloc", method = RequestMethod.POST)
    @ApiOperation("号码段删除")
    public BaseResponse<?> deleteMobileloc(@RequestBody Mobileloc mobileloc) {

        int success = -1;
        success = mobilelocService.delete(mobileloc);

        if (success > 0) {
            return BaseResponse.success("操作成功0.0");
        } else {
            return BaseResponse.failed("操作失败~_~");
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("号码段查询")
    public PageResponse<?> listByPage(@RequestBody MobileLocSearch mobileLocSearch) {
        PageInfo<Mobileloc> pageInfoMobileloc = mobilelocService.listByPage(mobileLocSearch);
        return PageResponse.successPageData(pageInfoMobileloc.getList(), pageInfoMobileloc.getTotal(), pageInfoMobileloc.getPageNum());
    }

    @RequestMapping(value = "/updateToRedis", method = RequestMethod.POST)
    @ApiOperation("号码段同步至redis")
    public BaseResponse<?> updateToRedis() {
        try {
            mobilelocService.updateToRedis();
            return BaseResponse.success("号码段同步至redis成功0.0");
        } catch (Exception e) {
            return BaseResponse.failed("号码段同步至redis错误~_~");
        }
    }

}
