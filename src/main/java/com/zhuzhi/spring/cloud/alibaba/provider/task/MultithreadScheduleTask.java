package com.zhuzhi.spring.cloud.alibaba.provider.task;

import com.zhuzhi.spring.cloud.alibaba.provider.domain.aliaccount.AliAccount;
import com.zhuzhi.spring.cloud.alibaba.provider.mapper.aliaccount.AliAccountMapper;
import com.zhuzhi.spring.cloud.alibaba.provider.shared.AliAccountShared;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

//@Component注解用于对那些比较中立的类进行注释；
//相对与在持久层、业务层和控制层分别采用 @Repository、@Service 和 @Controller 对分层中的类进行注释
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
@Slf4j
public class MultithreadScheduleTask {

    @Resource
    private AliAccountMapper aliAccountMapper;

    @Async
    @Scheduled(fixedDelay = 60000)  //间隔60秒
    public void first() throws InterruptedException {
        List<AliAccount> aliAccountList = aliAccountMapper.selectAll();
        if (aliAccountList != null && aliAccountList.size() > 0) {
            for (AliAccount aliAccount : aliAccountList) {
                for (int i = 0; i < 2; i++) {
                    AliAccountShared.aliAccountMap.put(AliAccountShared.ali_asr_lock_key + aliAccount.getAppkey() + "_" + i, aliAccount);
                }
            }
        }
        //log.info("阿里识别内存账号库定时任务开始 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName()+ "\r\ndata:"+AliAccountShared.aliAccountMap.toString());
    }

    /*@Async
    @Scheduled(fixedDelay = 2000)
    public void second() {
        System.out.println("第二个定时任务开始 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
        System.out.println();
    }*/
}