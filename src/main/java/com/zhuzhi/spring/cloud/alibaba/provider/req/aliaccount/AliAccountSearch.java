package com.zhuzhi.spring.cloud.alibaba.provider.req.aliaccount;
import com.zhuzhi.spring.cloud.alibaba.provider.req.ReqPageInfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class AliAccountSearch extends ReqPageInfo implements Serializable {

    private static final long serialVersionUID = -2754500895688037969L;
    private String appKey;
}
