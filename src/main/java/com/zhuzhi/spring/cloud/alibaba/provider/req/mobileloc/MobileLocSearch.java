package com.zhuzhi.spring.cloud.alibaba.provider.req.mobileloc;
import com.zhuzhi.spring.cloud.alibaba.provider.req.ReqPageInfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class MobileLocSearch extends ReqPageInfo implements Serializable {

    private static final long serialVersionUID = 3864144440836293026L;
    private String mobileNumber;
}
