package com.devotion.healthmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devotion.healthmanagement.entity.Body;
import com.devotion.healthmanagement.mapper.BodyMapper;
import com.devotion.healthmanagement.service.BodyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BodyServiceImpl extends ServiceImpl<BodyMapper, Body>  implements BodyService {
    @Autowired
    BodyMapper bodyMapper;

    @Override
    public boolean saveUpdate(Body userBody) {
        Body body = bodyMapper.selectOne(new QueryWrapper<Body>().eq("id",userBody.getId()).eq("update_time",userBody.getUpdateTime()));
        if(body == null){
            return bodyMapper.insert(userBody) > 0;
        }else{
            return bodyMapper.update(userBody, new UpdateWrapper<Body>().eq("id",userBody.getId()).eq("update_time",userBody.getUpdateTime())) > 0;
        }
    }
}
