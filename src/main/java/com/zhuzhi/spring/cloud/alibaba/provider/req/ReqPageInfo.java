package com.zhuzhi.spring.cloud.alibaba.provider.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReqPageInfo implements Serializable {

    private static final long serialVersionUID = -5214773646203936050L;

    private String page;

    private String limit;

    private String varLable;

    private String varName;

    private String token;

}
