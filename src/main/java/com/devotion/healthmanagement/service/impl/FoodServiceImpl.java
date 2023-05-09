package com.devotion.healthmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devotion.healthmanagement.entity.Food;
import com.devotion.healthmanagement.entity.dto.UserFood;
import com.devotion.healthmanagement.mapper.FoodMapper;
import com.devotion.healthmanagement.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {

    @Autowired
    FoodMapper foodMapper;

    @Override
    public List<UserFood> list(Integer id, String date) {
        return foodMapper.selectUserFoodList(id,date);
    }
}
