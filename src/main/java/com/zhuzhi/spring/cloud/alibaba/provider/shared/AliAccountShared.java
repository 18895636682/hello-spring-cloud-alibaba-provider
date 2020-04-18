package com.zhuzhi.spring.cloud.alibaba.provider.shared;

import com.zhuzhi.spring.cloud.alibaba.provider.domain.aliaccount.AliAccount;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AliAccountShared {

    //阿里账号信息存储
    public static volatile Map<String, AliAccount> aliAccountMap = new ConcurrentHashMap<String, AliAccount>();

    //获取账号时的锁key前缀
    public static volatile String ali_asr_lock_key = "ali_asr_lock_key_";

}
