package com.zhuzhi.spring.cloud.alibaba.provider.service.aliaccount.impl;

import com.alibaba.nacos.client.utils.StringUtils;
import com.zhuzhi.spring.cloud.alibaba.provider.common.BaseResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.common.aliasr.AliAsrResponse;
import com.zhuzhi.spring.cloud.alibaba.provider.domain.aliaccount.AliAccount;
import com.zhuzhi.spring.cloud.alibaba.provider.req.aliaccount.AliAccountSearch;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhuzhi.spring.cloud.alibaba.provider.mapper.aliaccount.AliAccountMapper;
import com.zhuzhi.spring.cloud.alibaba.provider.service.aliaccount.AliAccountService;
import com.zhuzhi.spring.cloud.alibaba.provider.shared.AliAccountShared;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AliAccountServiceImpl implements AliAccountService {

    @Resource
    private AliAccountMapper aliAccountMapper;

    @Autowired
    @Qualifier("redisson")
    private RedissonClient redissonClient;

    @Override
    public int create(AliAccount aliAccount) {
        aliAccount.setAddtime(new Date());
        return aliAccountMapper.insertSelective(aliAccount);
    }

    @Override
    public int update(AliAccount aliAccount) {
        aliAccount.setEdittime(new Date());
        return aliAccountMapper.updateByPrimaryKeySelective(aliAccount);
    }

    @Override
    public int delete(AliAccount aliAccount) {
        return aliAccountMapper.delete(aliAccount);
    }

    @Override
    public PageInfo<AliAccount> listByPage(AliAccountSearch aliAccountSearch) {
        Example example = new Example(AliAccount.class);
        Example.Criteria criteria = example.createCriteria();
        // 添加查询条件
        if (StringUtils.isNotBlank(aliAccountSearch.getAppKey()) && StringUtils.isNotBlank(aliAccountSearch.getAppKey().trim())) {

            criteria.andEqualTo("appkey", aliAccountSearch.getAppKey().trim());
            int count = aliAccountMapper.selectCount(new AliAccount(aliAccountSearch.getAppKey().trim()));

            int limitCount = Integer.parseInt(aliAccountSearch.getLimit());
            int currentPage = 0;
            if ((count % limitCount) > 0) {
                currentPage = (count / limitCount) + 1;
            } else {
                currentPage = count / limitCount;
            }

            if (Integer.parseInt(aliAccountSearch.getPage()) > currentPage) {
                aliAccountSearch.setPage("1");
            }
        }
        PageHelper.startPage(Integer.parseInt(aliAccountSearch.getPage()), Integer.parseInt(aliAccountSearch.getLimit()));
        PageInfo<AliAccount> pageInfoAliAccount = new PageInfo<>(aliAccountMapper.selectByExample(example));

        return pageInfoAliAccount;
    }

    @Override
    public BaseResponse<AliAsrResponse> getAliAsr() {
        BaseResponse<AliAsrResponse> result = null;
        try {
            //获取ali账号的全部锁keys
            Iterable<String> istr = redissonClient.getKeys().getKeysByPattern(AliAccountShared.ali_asr_lock_key + "*");

            //复制临界资源中的账号库
            Map<String, AliAccount> stringAliAccountMap = new HashMap<>();
            if (AliAccountShared.aliAccountMap != null && !AliAccountShared.aliAccountMap.isEmpty()) {
                stringAliAccountMap.putAll(AliAccountShared.aliAccountMap);
            }

            if (stringAliAccountMap != null && !stringAliAccountMap.isEmpty()) {
                //获取所有账号中没有被锁的key 存入list
                ArrayList<String> noLockKey = new ArrayList<String>();
                noLockKey.addAll(stringAliAccountMap.keySet());

                for (String string : stringAliAccountMap.keySet()) {
                    Boolean isLock = false;
                    for (String str : istr) {
                        if (str.equals(string)) {
                            isLock = true;
                            break;
                        }
                    }
                    if (isLock) {
                        noLockKey.remove(string);
                    }
                }

                //在redis未锁定的key中 随机选择一个key  获取锁
                if (noLockKey.size() > 0) {
                    String reskey = noLockKey.get(new Random().nextInt(noLockKey.size()));
                    final RLock lock = redissonClient.getLock(reskey);

                    if (lock.isLocked()) {
                        //找不到锁 再来一次
                        noLockKey.remove(reskey);

                        if (noLockKey.size() > 0) {
                            reskey = noLockKey.get(new Random().nextInt(noLockKey.size()));
                            final RLock lock1 = redissonClient.getLock(reskey);

                            if (lock1.isLocked()) {
                                //清內存
                                stringAliAccountMap.clear();
                                noLockKey.clear();
                                //找不到可用的ali识别账号
                                result = BaseResponse.failed("no_ali_asr_account");
                            } else {
                                boolean getLock = lock1.tryLock(500L, 2000L, TimeUnit.MILLISECONDS);
                                if (!getLock) {
                                    //清內存
                                    stringAliAccountMap.clear();
                                    noLockKey.clear();
                                    //找不到可用的ali识别账号
                                    result = BaseResponse.failed("no_ali_asr_account_timeout");
                                } else {
                                    AliAsrResponse aliAsrResponse = new AliAsrResponse();
                                    aliAsrResponse.setAppkey(stringAliAccountMap.get(reskey).getAppkey());
                                    aliAsrResponse.setAccessKeyID(stringAliAccountMap.get(reskey).getAccesskeyid());
                                    aliAsrResponse.setAccessKeySecret(stringAliAccountMap.get(reskey).getAccesskeysecret());
                                    //清內存
                                    stringAliAccountMap.clear();
                                    noLockKey.clear();
                                    result = BaseResponse.successData(aliAsrResponse);
                                }
                            }
                        } else {
                            //清內存
                            stringAliAccountMap.clear();
                            //找不到可用的ali识别账号
                            result = BaseResponse.failed("no_ali_asr_account");
                        }

                    } else {
                        boolean getLock = lock.tryLock(500L, 2000L, TimeUnit.MILLISECONDS);
                        if (!getLock) {
                            //清內存
                            stringAliAccountMap.clear();
                            noLockKey.clear();
                            //找不到可用的ali识别账号
                            result = BaseResponse.failed("no_ali_asr_account_timeout");
                        } else {
                            AliAsrResponse aliAsrResponse = new AliAsrResponse();
                            aliAsrResponse.setAppkey(stringAliAccountMap.get(reskey).getAppkey());
                            aliAsrResponse.setAccessKeyID(stringAliAccountMap.get(reskey).getAccesskeyid());
                            aliAsrResponse.setAccessKeySecret(stringAliAccountMap.get(reskey).getAccesskeysecret());

                            //清內存
                            stringAliAccountMap.clear();
                            noLockKey.clear();
                            result = BaseResponse.successData(aliAsrResponse);
                        }
                    }
                } else {
                    //清內存
                    stringAliAccountMap.clear();
                    //找不到可用的ali识别账号
                    result = BaseResponse.failed("no_ali_asr_account");
                }
            } else {
                //找不到可用的ali识别账号
                result = BaseResponse.failed("no_ali_asr_account");
            }
        } catch (Exception e) {
            log.error("zhuz: error:" + e);
            result = BaseResponse.failed("exception error");
        }

        log.info("zhuz: " + result.toString());
        return result;
    }
}
