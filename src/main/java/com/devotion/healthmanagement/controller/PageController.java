package com.devotion.healthmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.User;
import com.devotion.healthmanagement.entity.UserHealth;
import com.devotion.healthmanagement.service.HealthService;
import com.devotion.healthmanagement.service.UserService;
import com.devotion.healthmanagement.utils.RSA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Collection;

@Slf4j
@Controller
public class PageController {

    @Autowired
    RSA rsa;
    @Autowired
    UserService userService;
    @Autowired
    HealthService healthService;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("publicKey",rsa.publicKey);
        return "login";
    }
    @GetMapping("/register")
    public String toRegisterPage(){
        return "register";
    }

    @GetMapping("/todolist")
    public String todolistPage(){
        return "todolist";
    }

    @GetMapping("/index")
    public String toIndexPage(){
        return "index";
    }

    @GetMapping("/manage")
    public String toManagePage(){
        return "manage";
    }

    @GetMapping("/user_main")
    public String toMainPage(){
        return "user_main";
    }



    @GetMapping("/user")
    public String toUser(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        model.addAttribute("user",userService.getById((Serializable) session.getAttribute("id")));
        return "user";
    }

    @GetMapping("/password")
    public String toPassword(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        model.addAttribute("user",userService.getById((Serializable) session.getAttribute("id")));
        return "password";
    }
}
