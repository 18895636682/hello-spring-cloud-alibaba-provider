package com.zhuzhi.spring.cloud.alibaba.provider.common.aliasr;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AliAsrResponse {

    private String appkey;
    private String accessKeyID;
    private String accessKeySecret;
}
