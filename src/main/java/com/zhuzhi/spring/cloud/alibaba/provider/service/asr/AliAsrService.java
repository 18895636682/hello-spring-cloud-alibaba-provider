package com.zhuzhi.spring.cloud.alibaba.provider.service.asr;

import com.zhuzhi.spring.cloud.alibaba.provider.common.BaseResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.req.asr.AsrMeta;

public interface AliAsrService {
    BaseResponse<String> asr(AsrMeta asrMeta);
}
