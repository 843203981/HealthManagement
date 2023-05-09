package com.devotion.healthmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devotion.healthmanagement.entity.Food;
import com.devotion.healthmanagement.entity.dto.UserFood;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FoodMapper extends BaseMapper<Food> {
    @Select("select id,food_name,heat,carbons,proteins,fats,part,date from user_food where id = #{id} and date = #{date}")
    List<UserFood> selectUserFoodList(Integer id, String date);
}