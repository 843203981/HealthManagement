package com.devotion.healthmanagement.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.dto.UserFood;
import com.devotion.healthmanagement.service.BodyService;
import com.devotion.healthmanagement.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
        Integer fats = 0;
        Integer proteins = 0;
        Integer carbons = 0;
        Integer fatCount = bodyService.getById(id).getFatCount();
        Integer proteinCount = bodyService.getById(id).getProteinCount();
        Integer carbonCount = bodyService.getById(id).getCarbonCount();

        for (UserFood userFood : userFoods) {
            fats += userFood.getFats();
            proteins += userFood.getProteins();
            carbons += userFood.getCarbons();
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
        log.info("fats:"+fats.toString()+";proteins:"+proteins.toString()+";carbons:"+carbons.toString());
        log.info("fatCount:"+fatCount.toString()+";proteinCount:"+proteinCount.toString()+";carbonCount:"+carbonCount.toString());
        model.addAttribute("userFoodsMorning",userFoodsMorning);
        model.addAttribute("userFoodsNoon",userFoodsNoon);
        model.addAttribute("userFoodsNight",userFoodsNight);
        model.addAttribute("fats",fats);
        model.addAttribute("proteins",proteins);
        model.addAttribute("carbons",carbons);
        model.addAttribute("fatCount",fatCount);
        model.addAttribute("proteinCount",proteinCount);
        model.addAttribute("carbonCount",carbonCount);
        model.addAttribute("foods",foodService.list());
        return "eat";
    }
}
