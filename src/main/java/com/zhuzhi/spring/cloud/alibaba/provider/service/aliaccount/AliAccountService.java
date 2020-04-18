package com.zhuzhi.spring.cloud.alibaba.provider.service.aliaccount;

import com.zhuzhi.spring.cloud.alibaba.provider.common.BaseResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.common.aliasr.AliAsrResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.domain.aliaccount.AliAccount;
import com.zhuzhi.spring.cloud.alibaba.provider.req.aliaccount.AliAccountSearch;
import com.github.pagehelper.PageInfo;

public interface AliAccountService {


    int create(AliAccount aliAccount);

    int update(AliAccount aliAccount);

    int delete(AliAccount aliAccount);

    PageInfo<AliAccount> listByPage(AliAccountSearch aliAccountSearch);

    BaseResponse<AliAsrResponse> getAliAsr();
}
