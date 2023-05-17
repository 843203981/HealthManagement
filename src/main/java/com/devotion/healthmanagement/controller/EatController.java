package com.devotion.healthmanagement.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.Body;
import com.devotion.healthmanagement.entity.Food;
import com.devotion.healthmanagement.entity.dto.UserFood;
import com.devotion.healthmanagement.service.BodyService;
import com.devotion.healthmanagement.service.FoodService;
import com.devotion.healthmanagement.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/eat")
public class EatController {

    @Autowired
    FoodService foodService;

    @Autowired
    BodyService bodyService;

    @Autowired
    DateUtil dateUtil;

    @GetMapping("")
    public String toEatPage(Model model, HttpServletRequest request){
        Integer id = Integer.valueOf((String) request.getSession().getAttribute("id"));
        Date date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<UserFood> userFoods = foodService.list(id,sdf.format(date));
        List<Body> bodies = bodyService.list(new QueryWrapper<Body>().eq("id",id).orderByDesc("update_time"));
        Body body = bodies.get(0);

        UserFood userFoodDay = foodService.getUserFoodDate(userFoods);
        UserFood userFoodCount = foodService.getUserFoodCount(body);

        List<List<UserFood>> userFoodsList = foodService.partList(userFoods);
        List<UserFood> userFoodsMorning = userFoodsList.get(0);
        List<UserFood> userFoodsNoon = userFoodsList.get(1);
        List<UserFood> userFoodsNight = userFoodsList.get(2);

        log.info(userFoods.toString());
        log.info(userFoodsMorning.toString());
        log.info(userFoodsNoon.toString());
        log.info(userFoodsNight.toString());
        log.info("heats"+userFoodDay.getHeat()+":fats:"+ userFoodDay.getFats() +";proteins:"+ userFoodDay.getProteins()+";carbons:"+userFoodDay.getCarbons());
        log.info("heatCount"+userFoodCount.getHeat()+"fatCount:"+userFoodCount.getFats()+";proteinCount:"+userFoodCount.getProteins()+";carbonCount:"+userFoodCount.getCarbons());
        model.addAttribute("userFoodsMorning",userFoodsMorning);
        model.addAttribute("userFoodsNoon",userFoodsNoon);
        model.addAttribute("userFoodsNight",userFoodsNight);
        model.addAttribute("fats",userFoodDay.getFats());
        model.addAttribute("proteins",userFoodDay.getProteins());
        model.addAttribute("carbons",userFoodDay.getCarbons());
        model.addAttribute("heats",userFoodDay.getHeat());
        model.addAttribute("fatCount",userFoodCount.getFats());
        model.addAttribute("proteinCount",userFoodCount.getProteins());
        model.addAttribute("carbonCount",userFoodCount.getCarbons());
        model.addAttribute("heatCount",userFoodCount.getHeat());
        model.addAttribute("foods",foodService.list());
        return "eat";
    }

    @ResponseBody
    @PostMapping("/add")
    public String addUserFood(@RequestParam String part, String foodInput, HttpServletRequest request){
        String[] food = foodInput.split(",");
        List<UserFood> userFoods = new ArrayList<>();
        for (String s : food) {
            Food food1 = foodService.match(s);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            UserFood userFood = new UserFood();
            userFood.setId(Integer.valueOf((String) request.getSession().getAttribute("id")));
            userFood.setPart(part);
            userFood.setFoodName(s);
            userFood.setFats(food1.getFats());
            userFood.setProteins(food1.getProteins());
            userFood.setCarbons(food1.getCarbons());
            userFood.setHeat(food1.getHeat());
            userFood.setFoodId(food1.getFoodId());
            userFood.setDate(sdf.format(new Date()));
            userFoods.add(userFood);

        }
        foodService.saveUserFoods(userFoods);

        return "{\"msg\":\"添加成功\"}";

    }


}
