package com.zhuzhi.spring.cloud.alibaba.provider.service.impl;

import com.alibaba.nacos.client.utils.StringUtils;
import com.zhuzhi.spring.cloud.alibaba.provider.domain.Mobileloc;
import com.zhuzhi.spring.cloud.alibaba.provider.req.mobileloc.MobileLocSearch;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhuzhi.spring.cloud.alibaba.provider.shared.MobilelocShared;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.zhuzhi.spring.cloud.alibaba.provider.mapper.MobilelocMapper;
import com.zhuzhi.spring.cloud.alibaba.provider.service.MobilelocService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class MobilelocServiceImpl implements MobilelocService {

    @Resource
    private MobilelocMapper mobilelocMapper;

    @Autowired
    @Qualifier("redisson1")
    private RedissonClient redissonClient;

    @Override
    public int create(Mobileloc mobileloc) {
        return mobilelocMapper.insertSelective(mobileloc);
    }

    @Override
    public PageInfo<Mobileloc> listByPage(MobileLocSearch mobileLocSearch) {

        Example example = new Example(Mobileloc.class);
        Example.Criteria criteria = example.createCriteria();
        // 添加查询条件
        if (StringUtils.isNotBlank(mobileLocSearch.getMobileNumber()) && StringUtils.isNotBlank(mobileLocSearch.getMobileNumber().trim())) {
            criteria.andEqualTo("mobilenumber", mobileLocSearch.getMobileNumber().trim());
            int count = mobilelocMapper.selectCount(new Mobileloc(mobileLocSearch.getMobileNumber().trim()));

            int limitCount = Integer.parseInt(mobileLocSearch.getLimit());
            int currentPage = 0;
            if ((count % limitCount) > 0) {
                currentPage = (count / limitCount) + 1;
            } else {
                currentPage = count / limitCount;
            }

            if (Integer.parseInt(mobileLocSearch.getPage()) > currentPage) {
                mobileLocSearch.setPage("1");
            }
        }
        PageHelper.startPage(Integer.parseInt(mobileLocSearch.getPage()), Integer.parseInt(mobileLocSearch.getLimit()));
        PageInfo<Mobileloc> pageInfoMobileloc = new PageInfo<>(mobilelocMapper.selectByExample(example));

        return pageInfoMobileloc;
    }

    @Override
    public int delete(Mobileloc mobileloc) {
        return mobilelocMapper.delete(mobileloc);
    }

    @Override
    public int update(Mobileloc mobileloc) {
        return mobilelocMapper.updateByPrimaryKeySelective(mobileloc);
    }

    @Override
    public Mobileloc selectOne(Mobileloc mobileloc) {
        return mobilelocMapper.selectOne(mobileloc);
    }

    @Override
    public void updateToRedis() {
        List<Mobileloc> mobilelocList = mobilelocMapper.selectAll();
        if (mobilelocList != null && mobilelocList.size() > 0) {
            for (Mobileloc mobileloc : mobilelocList) {
                //将号码前七位与区号存入redis
                redissonClient.getBucket(MobilelocShared.mobileloc_key + mobileloc.getMobilenumber()).set(mobileloc.getAreacode());
            }
        }
        mobilelocList.clear();
    }
}
