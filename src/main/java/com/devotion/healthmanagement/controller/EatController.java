package com.devotion.healthmanagement.controller;


import com.devotion.healthmanagement.entity.Food;
import com.devotion.healthmanagement.entity.dto.UserFood;
import com.devotion.healthmanagement.service.BodyService;
import com.devotion.healthmanagement.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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



    @RequestMapping("")
    public String toEatPage(Model model, HttpServletRequest request){
        Integer id = Integer.valueOf((String) request.getSession().getAttribute("id"));
        Date date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<UserFood> userFoods = foodService.list(id,sdf.format(date));
        List<UserFood> userFoodsMorning = new ArrayList<>();
        List<UserFood> userFoodsNoon = new ArrayList<>();
        List<UserFood> userFoodsNight = new ArrayList<>();
        int fats = 0;
        int proteins = 0;
        int carbons = 0;
        int heats = 0;
        Integer fatCount = bodyService.getById(id).getFatCount();
        Integer proteinCount = bodyService.getById(id).getProteinCount();
        Integer carbonCount = bodyService.getById(id).getCarbonCount();

        for (UserFood userFood : userFoods) {
            if(userFood.getFats()==null){
                userFood.setFats(0);
            }else {
                fats += userFood.getFats();
            }
            if(userFood.getProteins()==null) {
                userFood.setProteins(0);
            }else {
                proteins += userFood.getProteins();
            }
            if(userFood.getCarbons()==null) {
                userFood.setCarbons(0);
            }else {
                carbons += userFood.getCarbons();
            }
            if(userFood.getHeat()==null){
                userFood.setHeat(0);
            }else{
                heats += userFood.getHeat();
            }
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

        log.info(userFoods.toString());
        log.info(userFoodsMorning.toString());
        log.info(userFoodsNoon.toString());
        log.info(userFoodsNight.toString());
        log.info("fats:"+ fats +";proteins:"+ proteins +";carbons:"+ carbons);
        log.info("fatCount:"+fatCount.toString()+";proteinCount:"+proteinCount.toString()+";carbonCount:"+carbonCount.toString());
        model.addAttribute("userFoodsMorning",userFoodsMorning);
        model.addAttribute("userFoodsNoon",userFoodsNoon);
        model.addAttribute("userFoodsNight",userFoodsNight);
        model.addAttribute("fats",fats);
        model.addAttribute("proteins",proteins);
        model.addAttribute("carbons",carbons);
        model.addAttribute("heats",heats);
        model.addAttribute("fatCount",fatCount);
        model.addAttribute("proteinCount",proteinCount);
        model.addAttribute("carbonCount",carbonCount);
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
