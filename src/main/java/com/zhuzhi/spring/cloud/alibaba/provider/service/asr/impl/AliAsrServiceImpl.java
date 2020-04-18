package com.zhuzhi.spring.cloud.alibaba.provider.service.asr.impl;

import com.zhuzhi.spring.cloud.alibaba.provider.common.BaseResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.common.aliasr.AliAsrResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.req.asr.AsrMeta;
import com.zhuzhi.spring.cloud.alibaba.provider.service.aliaccount.AliAccountService;
import com.zhuzhi.spring.cloud.alibaba.provider.service.asr.AliAsrService;
import com.zhuzhi.spring.cloud.alibaba.provider.shared.RecordFileShared;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
@Slf4j
public class AliAsrServiceImpl implements AliAsrService {

    @Autowired
    private AliAccountService aliAccountService;

    @Autowired
    @Qualifier("redisson2")
    private RedissonClient redissonClient;

    /**
     * 调用linux系统命令 并获取返回值
     */
    public BaseResponse<String> asr(AsrMeta asrMeta) {

        BaseResponse<String> res;

        try {
            Long time = System.currentTimeMillis();
            Boolean fileIsExists = false;
            while (System.currentTimeMillis() - time <= 2000L) {
                if (redissonClient.getBucket(RecordFileShared.record_file_key + asrMeta.getRecordFileName()).isExists()) {
                    log.info("当前需要识别的通话录音文件已存在 fileName:" + asrMeta.getRecordFileName());
                    fileIsExists = true;
                    break;
                }
                Thread.sleep(100L);
            }

            if (fileIsExists) {
                BaseResponse<AliAsrResponse> aliAsrResponse = aliAccountService.getAliAsr();
                String cmd = "timeout 20 /opt/fsgui/nway-sys/NlsSdkCpp2.0/demo/ali_asr " + aliAsrResponse.getData().getAppkey() + " " + aliAsrResponse.getData().getAccessKeyID() + " " + aliAsrResponse.getData().getAccessKeySecret() + " /usr/local/freeswitch/recordings/" + asrMeta.getRecordFileName();
                log.info("zhuz: " + cmd);
                Process ps = Runtime.getRuntime().exec(cmd);

                BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String result = sb.toString();

                log.info("zhuz: " + result);

                String message = result.split("[|]")[0];
                String code = result.split("[|]")[1];
                if ("0".equals(code)) {
                    res = BaseResponse.successData(message);
                } else {
                    res = BaseResponse.failed(message);
                }
            } else {
                log.info("zhuz: fileName is not exist");
                res = BaseResponse.failed("fileName is not exist");
            }
        } catch (Exception e) {
            log.error("zhuz: ----error-----" + e);
            res = BaseResponse.failed("asr_error");
        }

        return res;
    }
}
