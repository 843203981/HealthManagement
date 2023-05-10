package com.devotion.healthmanagement.controller;

import com.devotion.healthmanagement.entity.dto.UserFood;
import com.devotion.healthmanagement.entity.dto.UserSport;
import com.devotion.healthmanagement.service.SportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/exercise")
public class ExerciseController {

    @Autowired
    SportService sportService;
    @GetMapping("")
    public String toExercisePage(Model model, HttpServletRequest request){
        Integer id = Integer.valueOf((String) request.getSession().getAttribute("id"));
        Date date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<UserSport> userSports = sportService.list(id,sdf.format(date));
        int highCost = 0;
        int lowCost = 0;
        int middleCost = 0;

        for (UserSport userSport : userSports) {
            switch (userSport.getType()){
                case "无氧运动":
                    highCost += userSport.getCost()*userSport.getTime();
                    break;
                case "有氧运动":
                    middleCost += userSport.getCost()*userSport.getTime();
                    break;
                case "日常活动":
                    lowCost += userSport.getCost()*userSport.getTime();
                    break;
            }
        }
        int cost = highCost+middleCost+lowCost;
        if(cost != 0){
            highCost = highCost*100/cost;
            middleCost = middleCost*100/cost;
            lowCost = lowCost*100/cost;
        }
        model.addAttribute("highCost",highCost);
        model.addAttribute("middleCost",middleCost);
        model.addAttribute("lowCost",lowCost);
        model.addAttribute("userSports",userSports);
        return "exercise";
    }
}
