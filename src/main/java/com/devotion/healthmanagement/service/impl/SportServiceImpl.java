package com.devotion.healthmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    @Override
    public Sport match(String sportName) {
        /*Food food1 = new Food();
        food1.setFoodName(foodName);
        Food foodSelect = foodMapper.selectOne(new QueryWrapper<Food>().eq("food_name",foodName));
        if(foodSelect == null){
            foodMapper.insert(food1);
            foodSelect =  foodMapper.selectOne(new QueryWrapper<Food>().eq("food_name",foodName));
        }
        return foodSelect;*/
        Sport sport1 = new Sport();
        sport1.setSportName(sportName);
        Sport sportSelect = sportMapper.selectOne(new QueryWrapper<Sport>().eq("sport_name",sportName));
        if(sportSelect == null){
            sportMapper.insert(sport1);
            sportSelect =  sportMapper.selectOne(new QueryWrapper<Sport>().eq("sport_name",sportName));
        }
        return sportSelect;
    }

    @Override
    public void saveUserSport(UserSport userSport) {
        sportMapper.insertUserSport(userSport);
    }
}
