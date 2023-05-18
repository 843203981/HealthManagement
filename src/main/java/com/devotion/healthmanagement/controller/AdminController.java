package com.devotion.healthmanagement.controller;

import com.devotion.healthmanagement.service.*;
import com.devotion.healthmanagement.utils.DateUtil;
import com.devotion.healthmanagement.utils.MatchUtils;
import com.devotion.healthmanagement.utils.RSA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class AdminController {
    @Autowired
    RSA rsa;
    @Autowired
    UserService userService;
    @Autowired
    HealthService healthService;
    @Autowired
    DateUtil dateUtil;
    @Autowired
    SportService sportService;
    @Autowired
    FoodService foodService;
    @Autowired
    IllnessService illnessService;
    @Autowired
    MatchUtils matchUtils;
    @Autowired
    BodyService bodyService;
    @Autowired
    InfoService infoService;

    @GetMapping("/admin")
    public String toAdminPage(HttpServletRequest request){
        HttpSession session = request.getSession();
        if("1".equals(session.getAttribute("isAdmin"))){
            return "admin";
        }else{
            return "noAccess";
        }
    }

    @GetMapping("/userAdmin")
    public String toUserPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if("1".equals(session.getAttribute("isAdmin"))){
            userService.list();
            model.addAttribute("users",userService.list());
            return "userAdmin";
        }else{
            return "noAccess";
        }
    }

    @GetMapping("/foodAdmin")
    public String toFoodPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if("1".equals(session.getAttribute("isAdmin"))){
            model.addAttribute("foods",foodService.list());
            return "foodAdmin";
        }else{
            return "noAccess";
        }
    }

    @GetMapping("/sportAdmin")
    public String toSportPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if("1".equals(session.getAttribute("isAdmin"))){
            model.addAttribute("sports",sportService.list());
            return "sportAdmin";
        }else{
            return "noAccess";
        }
    }

    @GetMapping("/illAdmin")
    public String toIllPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if("1".equals(session.getAttribute("isAdmin"))){
            model.addAttribute("illnesses",illnessService.list());
            return "illAdmin";
        }else{
            return "noAccess";
        }
    }

    @GetMapping("/bodyAdmin")
    public String toBodyPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if("1".equals(session.getAttribute("isAdmin"))){
            model.addAttribute("bodies",bodyService.list());
            return "bodyAdmin";
        }else{
            return "noAccess";
        }
    }

    @GetMapping("/healthAdmin")
    public String toHealthPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if("1".equals(session.getAttribute("isAdmin"))){
            model.addAttribute("records",healthService.list());
            return "healthAdmin";
        }else{
            return "noAccess";
        }
    }

    @GetMapping("/eatAdmin")
    public String toEatPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if("1".equals(session.getAttribute("isAdmin"))){
            model.addAttribute("records",foodService.list());
            return "eatAdmin";
        }else{
            return "noAccess";
        }
    }

    @GetMapping("/infoAdmin")
    public String toInfoPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if("1".equals(session.getAttribute("isAdmin"))){
            model.addAttribute("infos",infoService.list());
            return "infoAdmin";
        }else{
            return "noAccess";
        }
    }

    @GetMapping("/adminIndex")
    public String toIndexPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if("1".equals(session.getAttribute("isAdmin"))){
            model.addAttribute("infos",infoService.list());
            return "adminIndex";
        }else{
            return "noAccess";
        }
    }
}
