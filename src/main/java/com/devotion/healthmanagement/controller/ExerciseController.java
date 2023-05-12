package com.devotion.healthmanagement.controller;

import com.alibaba.fastjson.JSON;
import com.devotion.healthmanagement.entity.Sport;
import com.devotion.healthmanagement.entity.dto.UserFood;
import com.devotion.healthmanagement.entity.dto.UserSport;
import com.devotion.healthmanagement.service.SportService;
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

        List<String> highSports = new ArrayList<>();
        List<String> lowSports = new ArrayList<>();
        List<String> dailySports = new ArrayList<>();

        List<Sport> sports = sportService.list();
        for (Sport sport : sports) {
            switch (sport.getType()){
                case "无氧运动":
                    highSports.add(sport.getSportName());
                    break;
                case "有氧运动":
                    lowSports.add(sport.getSportName());
                    break;
                case "日常活动":
                    dailySports.add(sport.getSportName());
                    break;
            }
        }
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

        log.info(dailySports.toString());
        log.info(lowSports.toString());
        log.info(highSports.toString());

        model.addAttribute("highSports",highSports);
        model.addAttribute("lowSports",lowSports);
        model.addAttribute("dailySports",dailySports);
        model.addAttribute("highCost",highCost);
        model.addAttribute("middleCost",middleCost);
        model.addAttribute("lowCost",lowCost);
        model.addAttribute("userSports",userSports);
        return "exercise";
    }

    @ResponseBody
    @PostMapping("/add")
    public String addExercise(@RequestParam String sportName, Integer time, HttpServletRequest request){

        Integer id = Integer.valueOf((String) request.getSession().getAttribute("id"));
        Date date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Sport sport = sportService.match(sportName);
        UserSport userSport = new UserSport();
        userSport.setId(id);
        userSport.setSportId(sport.getSportId());
        userSport.setCost(sport.getCost());
        userSport.setTime(time);
        userSport.setDate(sdf.format(date));
        userSport.setSportName(sportName);
        userSport.setType(sport.getType());
        sportService.saveUserSport(userSport);
        return "{\"msg\":\"添加成功\"}";
    }
}
