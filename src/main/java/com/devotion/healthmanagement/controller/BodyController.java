package com.devotion.healthmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.UserBody;
import com.devotion.healthmanagement.entity.UserHealth;
import com.devotion.healthmanagement.service.BodyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/body")
public class BodyController {

    @Autowired
    BodyService bodyService;

    @GetMapping("")
    public String toBodyPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        QueryWrapper<UserBody> wrapper = new QueryWrapper<>();
        wrapper.eq("id",session.getAttribute("id")).orderByDesc("update_time");
        model.addAttribute("userBody",bodyService.list(wrapper));
        log.info("healthService.list(wrapper)");
        return "body";
    }

    @ResponseBody
    @PostMapping("/add")
    public String addHealth(UserBody userBody){
        log.info(userBody.toString());
        if(bodyService.save(userBody)){
            return "{\"msg\":\"添加成功\"}";
        }else{
            return "{\"msg\":\"添加失败,请重试\"}";
        }

    }

}
