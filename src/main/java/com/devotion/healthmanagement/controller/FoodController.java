package com.devotion.healthmanagement.controller;

import com.devotion.healthmanagement.entity.Food;
import com.devotion.healthmanagement.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
