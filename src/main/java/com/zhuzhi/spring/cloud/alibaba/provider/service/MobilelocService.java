package com.zhuzhi.spring.cloud.alibaba.provider.service;

import com.zhuzhi.spring.cloud.alibaba.provider.domain.Mobileloc;
import com.zhuzhi.spring.cloud.alibaba.provider.req.mobileloc.MobileLocSearch;
import com.github.pagehelper.PageInfo;

public interface MobilelocService{


    int create(Mobileloc mobileloc);

    PageInfo<Mobileloc> listByPage(MobileLocSearch mobileLocSearch);

    int delete(Mobileloc mobileloc);

    int update(Mobileloc mobileloc);

    Mobileloc selectOne(Mobileloc mobileloc);

    void updateToRedis();
}
