package com.devotion.healthmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.Food;
import com.devotion.healthmanagement.entity.dto.Msg;
import com.devotion.healthmanagement.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/food")
public class FoodController {

    @Autowired
    FoodService foodService;

    @PostMapping("")
    public void toFoodPage(Model model){
        List<Food> foods =foodService.list();
        log.info(foods.toString());
        model.addAttribute("foods",foods);
    }

    @ResponseBody
    @PostMapping("/update")
    public Msg updateFood(Food food,Model model){
        log.info("更新食物"+food);
        Msg msg = new Msg();
        if(food == null){
            msg.setInfo("食物不能为空");
        }
        if(foodService.saveOrUpdate(food)){
            msg.setInfo("更新成功！");
        }
        else {
            msg.setInfo("更新失败！");
        }
        return msg;
    }

    @ResponseBody
    @PostMapping("/delete")
    public Msg deleteFood(Food food){
        log.info("删除食物"+food);
        Msg msg = new Msg();
        if(food.getFoodId() == null){
            msg.setInfo("食物不能为空");
            return msg;
        }
        if(foodService.removeById(food.getFoodId())){
            msg.setInfo("删除成功！");
        }
        else {
            msg.setInfo("删除失败！");
        }
        return msg;
    }

    @ResponseBody
    @PostMapping("/select")
    public Msg selectFood(@RequestParam String words){
        log.info("查询食物"+words);
        Msg msg = new Msg();
        List<Food> foods = foodService.list(new QueryWrapper<Food>().like("food_name",words));
        msg.setInfo("success");
        msg.setData(foods);
        return msg;
    }
}
