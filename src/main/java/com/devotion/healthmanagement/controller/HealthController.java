package com.devotion.healthmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devotion.healthmanagement.entity.UserHealth;
import com.devotion.healthmanagement.service.HealthService;
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
@RequestMapping("/health")
public class HealthController {

    @Autowired
    HealthService healthService;

    @GetMapping("")
    public String toHealthPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        //Page对象
        Page<UserHealth> page = new Page<>(1, 5);
        IPage<UserHealth> iPage = healthService.page(page);
        QueryWrapper<UserHealth> wrapper = new QueryWrapper<>();
        wrapper.eq("id",session.getAttribute("id")).orderByDesc("update_time");
        model.addAttribute("userHealth",healthService.list(wrapper));
        log.info("healthService.list(wrapper)");
        return "health";
    }

    @ResponseBody
    @PostMapping("/add")
    public String addHealth(UserHealth userHealth){
        log.info(userHealth.toString());
        if(healthService.save(userHealth)){
            return "{\"msg\":\"添加成功\"}";
        }else{
            return "{\"msg\":\"添加失败,请重试\"}";
        }

    }
}
