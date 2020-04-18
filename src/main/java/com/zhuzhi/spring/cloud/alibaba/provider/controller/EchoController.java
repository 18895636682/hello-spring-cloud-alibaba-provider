package com.zhuzhi.spring.cloud.alibaba.provider.controller;

import com.zhuzhi.spring.cloud.alibaba.provider.domain.Mobileloc;
import com.zhuzhi.spring.cloud.alibaba.provider.mapper.MobilelocMapper;
import com.zhuzhi.spring.cloud.alibaba.provider.shared.AliAccountShared;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {

    @Value("${server.port}")
    private String port;

    @Qualifier("redisson")
    @Autowired
    private RedissonClient redissonClient;

    @GetMapping(value = "/lb")
    public String lb() {
        return "Hello Nacos Provider i am from port: " + port;
    }

    @GetMapping(value = "/redissonClient/{string}")
    public String redissonClient(@PathVariable String string) {
        /*RBucket<String> keyObj = redissonClient.getBucket("k1" + string);
        keyObj.set(string);*/
        Iterable<String> istr = redissonClient.getKeys().getKeysByPattern(AliAccountShared.ali_asr_lock_key + "*");
        for (String str : istr) {
            System.out.println("--------------------" + str);
        }
        return "Hello Nacos Provider i am from port: " + port;
    }

    @Autowired
    private MobilelocMapper mobilelocMapper;

    @GetMapping(value = "/getMobilelocMapper/{string}")
    public String getMobilelocMapper(@PathVariable String string) {
        Mobileloc mobileloc = mobilelocMapper.selectByPrimaryKey(string);
        return "zhuz: port-" + port + " " + mobileloc.toString();
    }

    @GetMapping(value = "/echo/{string}")
    public String echo(@PathVariable String string) {
        return "Hello Nacos Provider " + string;
    }
}