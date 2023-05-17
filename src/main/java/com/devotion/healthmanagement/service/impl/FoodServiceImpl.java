package com.devotion.healthmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devotion.healthmanagement.entity.Body;
import com.devotion.healthmanagement.entity.Food;
import com.devotion.healthmanagement.entity.dto.UserFood;
import com.devotion.healthmanagement.mapper.BodyMapper;
import com.devotion.healthmanagement.mapper.FoodMapper;
import com.devotion.healthmanagement.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {

    @Autowired
    FoodMapper foodMapper;
    @Autowired
    BodyMapper bodyMapper;

    @Override
    public List<UserFood> list(Integer id, String date) {
        return foodMapper.selectUserFoodList(id,date);
    }

    @Override
    public Food match(String foodName) {
        Food food1 = new Food();
        food1.setFoodName(foodName);
        Food foodSelect = foodMapper.selectOne(new QueryWrapper<Food>().eq("food_name",foodName));
        if(foodSelect == null){
            foodMapper.insert(food1);
            foodSelect =  foodMapper.selectOne(new QueryWrapper<Food>().eq("food_name",foodName));
        }
        return foodSelect;

    }

    @Override
    public void saveUserFoods(List<UserFood> userFoods) {
        for (UserFood userFood : userFoods) {
            foodMapper.insertUserFood(userFood);
        }
    }

    @Override
    public UserFood getUserFoodDate(List<UserFood> userFoods) {
        for (UserFood userFood : userFoods) {
            Food food = foodMapper.selectById(userFood.getFoodId());
            userFood.setFoodName(food.getFoodName());
            userFood.setFats(food.getFats());
            userFood.setProteins(food.getProteins());
            userFood.setCarbons(food.getCarbons());
            userFood.setHeat(food.getHeat());
        }
        int fats = 0;
        int proteins = 0;
        int carbons = 0;
        int heats = 0;

        for (UserFood userFood : userFoods) {
            if (userFood.getFats() == null) {
                userFood.setFats(0);
            } else {
                fats += userFood.getFats();
            }
            if (userFood.getProteins() == null) {
                userFood.setProteins(0);
            } else {
                proteins += userFood.getProteins();
            }
            if (userFood.getCarbons() == null) {
                userFood.setCarbons(0);
            } else {
                carbons += userFood.getCarbons();
            }
            if (userFood.getHeat() == null) {
                userFood.setHeat(0);
            } else {
                heats += userFood.getHeat();
            }
        }
        UserFood userFood = new UserFood();
        userFood.setFats(fats);
        userFood.setProteins(proteins);
        userFood.setCarbons(carbons);
        userFood.setHeat(heats);
        return userFood;
    }

    @Override
    public List<List<UserFood>> partList(List<UserFood> userFoods) {
        List<List<UserFood>> partList = new ArrayList<>()  ;
        List<UserFood> userFoodsMorning = new ArrayList<>();
        List<UserFood> userFoodsNoon = new ArrayList<>();
        List<UserFood> userFoodsNight = new ArrayList<>();
        for (UserFood userFood : userFoods) {
            switch (userFood.getPart()){
                case("早餐"):
                    userFoodsMorning.add(userFood);
                    break;
                case("午餐"):
                    userFoodsNoon.add(userFood);
                    break;
                case("晚餐"):
                    userFoodsNight.add(userFood);
                    break;
            }
        }
        partList.add(userFoodsMorning);
        partList.add(userFoodsNoon);
        partList.add(userFoodsNight);
        return partList;
    }

    @Override
    public UserFood getUserFoodCount(Body body) {
        Integer fatCount = body.getFatCount();
        Integer proteinCount = body.getProteinCount();
        Integer carbonCount = body.getCarbonCount();
        Integer heatCount = body.getHeatCount();
        if(fatCount==null){
            fatCount = 40;
        }
        if (proteinCount==null){
            proteinCount = 45;
        }
        if(carbonCount==null){
            carbonCount = 200;
        }
        if(heatCount==null){
            heatCount = 1800;
        }
        UserFood userFood = new UserFood();
        userFood.setFats(fatCount);
        userFood.setProteins(proteinCount);
        userFood.setCarbons(carbonCount);
        userFood.setHeat(heatCount);
        return userFood;
    }
}
