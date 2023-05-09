package com.devotion.healthmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.Illness;
import com.devotion.healthmanagement.entity.dto.UserIllness;
import com.devotion.healthmanagement.mapper.IllnessMapper;
import com.devotion.healthmanagement.service.AsyncService;
import com.devotion.healthmanagement.utils.MatchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    IllnessMapper illnessMapper;

    @Autowired
    MatchUtils matchUtils;

    @Override
    @Transactional
    @Async("asyncServiceExecutor")
    public void asyncUpload(List<UserIllness> userIllnessList) {
        log.info("当前线程的id为:"+Thread.currentThread().getName()+"处理的数据内容为:"+userIllnessList);
        if (null != userIllnessList && userIllnessList.size() > 0) {
            for (UserIllness userIll : userIllnessList) {
                Lock lock=new ReentrantLock();
                lock.lock();
                try {
                    matchUtils.matchIllIdByIllName(userIll);
                    illnessMapper.deleteUserIllness(userIll);
                    illnessMapper.insertUserIllness(userIll);
                } catch (Exception e) {
                    //记录出现异常的时间，线程name
                    log.info("fail,time=" + System.currentTimeMillis() + ",thread id=" + Thread.currentThread().getName() );
                }
                finally {
                    lock.unlock();
                }
            }
        }
    }
}
