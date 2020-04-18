package com.zhuzhi.spring.cloud.alibaba.provider.common;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException {
    private String code;
    private String msg;

    public BusinessException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String msg) {
        this.msg = msg;
    }
}
