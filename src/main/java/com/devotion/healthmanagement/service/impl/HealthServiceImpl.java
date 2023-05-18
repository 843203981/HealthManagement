package com.devotion.healthmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devotion.healthmanagement.entity.Body;
import com.devotion.healthmanagement.entity.Health;
import com.devotion.healthmanagement.mapper.HealthMapper;
import com.devotion.healthmanagement.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthServiceImpl extends ServiceImpl<HealthMapper, Health> implements HealthService {
    @Autowired
    HealthMapper healthMapper;

    @Override
    public boolean saveUpdate(Health userHealth) {
        Health health = healthMapper.selectOne(new QueryWrapper<Health>().eq("id",userHealth.getId()).eq("update_time",userHealth.getUpdateTime()));
        if(health == null){
            return healthMapper.insert(userHealth) > 0;
        }else{
            return healthMapper.update(userHealth, new UpdateWrapper<Health>().eq("id",userHealth.getId()).eq("update_time",userHealth.getUpdateTime())) > 0;
        }
    }
}
