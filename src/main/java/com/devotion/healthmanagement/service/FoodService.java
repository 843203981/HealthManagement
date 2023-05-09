package com.devotion.healthmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.devotion.healthmanagement.entity.Food;
import com.devotion.healthmanagement.entity.dto.UserFood;

import java.util.Date;
import java.util.List;

public interface FoodService extends IService<Food>{
    public List<UserFood> list(Integer id, String date);
}
