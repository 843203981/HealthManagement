package com.devotion.healthmanagement.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.Illness;
import com.devotion.healthmanagement.entity.dto.UserIllness;
import com.devotion.healthmanagement.mapper.IllnessMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MatchUtils {

    @Autowired
    IllnessMapper illnessMapper;

    public UserIllness matchIllIdByIllName(UserIllness userIllness){
        QueryWrapper<Illness> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ill_name", userIllness.getIllName());
        Illness illness = illnessMapper.selectOne(queryWrapper);
        if(illness == null){
            illness = new Illness();
            illness.setIllName(userIllness.getIllName());
            illnessMapper.insert(illness);
        }
        userIllness.setIllId(illnessMapper.selectOne(queryWrapper).getIllId());
        return userIllness;
    }
}
