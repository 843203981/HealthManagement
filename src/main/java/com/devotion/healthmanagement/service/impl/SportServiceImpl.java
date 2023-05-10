package com.devotion.healthmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devotion.healthmanagement.entity.Sport;
import com.devotion.healthmanagement.entity.dto.UserSport;
import com.devotion.healthmanagement.mapper.SportMapper;
import com.devotion.healthmanagement.service.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportServiceImpl extends ServiceImpl<SportMapper, Sport> implements SportService {

    @Autowired
    SportMapper sportMapper;
    @Override
    public List<UserSport> list(Integer id, String date) {
        return sportMapper.selectUserSportList(id,date);
    }
}
