package com.devotion.healthmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.Body;
import com.devotion.healthmanagement.entity.Health;
import com.devotion.healthmanagement.entity.Illness;
import com.devotion.healthmanagement.entity.Info;
import com.devotion.healthmanagement.entity.dto.Msg;
import com.devotion.healthmanagement.entity.dto.UserFood;
import com.devotion.healthmanagement.entity.dto.UserSport;
import com.devotion.healthmanagement.service.*;
import com.devotion.healthmanagement.utils.DateUtil;
import com.devotion.healthmanagement.utils.MatchUtils;
import com.devotion.healthmanagement.utils.RSA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class PageController {

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
    public String toMainPage(Model model, HttpServletRequest request){
        Integer id = Integer.valueOf((String) request.getSession().getAttribute("id"));
        QueryWrapper<Health> healthQueryWrapper = new QueryWrapper<>();
        healthQueryWrapper.eq("id",id).orderByDesc("update_time");
        List<Health> healthList = healthService.list(healthQueryWrapper);
        Health healthLatest = healthList.get(0);

        Date date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<UserFood> userFoods = foodService.list(id,sdf.format(date));
        UserFood userFoodDay = foodService.getUserFoodDate(userFoods);

        List<Body> bodies = bodyService.list(new QueryWrapper<Body>().eq("id",id).orderByDesc("update_time"));
        Body body = bodies.get(0);

        UserFood userFoodCount = foodService.getUserFoodCount(body);
        List<Health> healths = healthService.list(new QueryWrapper<Health>().eq("id",id).orderByDesc("update_time"));
        Health userHealth = healths.get(0);
        List<Msg> msgList = matchUtils.matchUserHealthByHealth(userHealth);
        if(msgList.size() == 1 || msgList.get(0).getName().equals("健康")){
            model.addAttribute("msg", "您的健康状况良好，继续保持");
        }else{
            model.addAttribute("msg", "建议您早睡早起，规律饮食，提升良好的健康水平");
        }
        List<UserSport> userSports = sportService.list(id,sdf.format(date));
        int cost = 0;
        for (UserSport userSport : userSports) {
            cost += userSport.getCost()*userSport.getTime();
        }
        model.addAttribute("cost",cost);
        log.info(userFoodDay.getHeat()*100/userFoodCount.getHeat()+" ");
        model.addAttribute("fatPercent",userFoodDay.getFats()*100/userFoodCount.getFats());
        model.addAttribute("proteinPercent",userFoodDay.getProteins()*100/userFoodCount.getProteins());
        model.addAttribute("carbonPercent",userFoodDay.getCarbons()*100/userFoodCount.getCarbons());
        model.addAttribute("heatPercent",userFoodDay.getHeat()*100/userFoodCount.getHeat());

        model.addAttribute("date",healthLatest.getUpdateTime());

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

    @GetMapping("/exerciseAnalysis")
    public String toExerciseAnalysisPage(Model model, HttpServletRequest request) throws ParseException {
        Integer id = Integer.valueOf((String) request.getSession().getAttribute("id"));
        List<Integer> costs = new ArrayList<>();
        List<String> dates = dateUtil.getSevenDate();
        /*List<String> dateList = new ArrayList<>();*/
        for (String date : dates) {
            List<UserSport> userSports = sportService.list(id,date);
            int cost = 0;
            for (UserSport userSport : userSports) {
                cost += userSport.getCost()*userSport.getTime();
            }
            costs.add(cost);
        }
        log.info(costs.toString());
        log.info(dates.toString());
        model.addAttribute("costs", costs);
        model.addAttribute("dates", dates);
        return "exerciseAnalysis";
    }

    @GetMapping("/eatAnalysis")
    public String toAnalysisPage(Model model, HttpServletRequest request){
        Integer id = Integer.valueOf((String) request.getSession().getAttribute("id"));
        List<String> dates = dateUtil.getSevenDate();
        List<UserFood> userFoods = new ArrayList<>();
        List<Integer> heatDates = new ArrayList<>();
        List<Integer> fatDates = new ArrayList<>();
        List<Integer> proteinDates = new ArrayList<>();
        List<Integer> carbonDates = new ArrayList<>();

        for (String date : dates) {
            int heatDate = 0;
            int fatDate = 0;
            int proteinDate = 0;
            int carbonDate = 0;
            List<UserFood> userFoodDate = foodService.list(id,date);
            for (UserFood userFood : userFoodDate) {
                heatDate += userFood.getHeat();
                fatDate += userFood.getFats();
                proteinDate += userFood.getProteins();
                carbonDate += userFood.getCarbons();
            }
            UserFood userFood = new UserFood();
            userFood.setHeat(heatDate);
            userFood.setFats(fatDate);
            userFood.setProteins(proteinDate);
            userFood.setCarbons(carbonDate);
            userFood.setId(id);
            userFood.setDate(date);
            userFoods.add(userFood);
        }
        for (UserFood userFood : userFoods) {
            heatDates.add(userFood.getHeat());
            fatDates.add(userFood.getFats());
            proteinDates.add(userFood.getProteins());
            carbonDates.add(userFood.getCarbons());
        }


        model.addAttribute("heatDates",heatDates);
        model.addAttribute("fatDates",fatDates);
        model.addAttribute("proteinDates",proteinDates);
        model.addAttribute("carbonDates",carbonDates);
        model.addAttribute("dateList",dates);
        return "eatAnalysis";
    }

    @GetMapping("/illnessAnalysis")
    public String toIllnessAnalysisPage(Model model, HttpServletRequest request){
        Integer id = Integer.valueOf((String) request.getSession().getAttribute("id"));
        List<Health> healths = healthService.list(new QueryWrapper<Health>().eq("id",id).orderByDesc("update_time"));
        Health userHealth = healths.get(0);
        Illness health = illnessService.getById("1");
        model.addAttribute("health",health);
        model.addAttribute("user",userHealth);
        List<Msg> msgList = matchUtils.matchUserHealthByHealth(userHealth);
        log.info(msgList.toString());
        model.addAttribute("msgList",msgList);
        return "illnessAnalysis";
    }

    @GetMapping("/healthAnalysis")
    public String toHealthAnalysisPage(Model model, HttpServletRequest request){
        Integer id = Integer.valueOf((String) request.getSession().getAttribute("id"));
        List<Body> bodies = bodyService.list(new QueryWrapper<Body>().eq("id",id).orderByDesc("update_time"));
        Body userBody = bodies.get(0);
        model.addAttribute("user",userBody);
        return "healthAnalysis";
    }

    @GetMapping("/healthAdvice")
    public String toHealthAdvicePage(Model model){
        List<Info> infoList = infoService.list(new QueryWrapper<Info>().eq("type", "健康生活建议"));
        model.addAttribute("infoList",infoList);
        return "healthAdvice";
    }

    @GetMapping("/eatAdvice")
    public String toEatAdvicePage(Model model){
        List<Info> infoList = infoService.list(new QueryWrapper<Info>().eq("type", "健康饮食建议"));
        model.addAttribute("infoList",infoList);
        return "eatAdvice";
    }

    @GetMapping("/illAdvice")
    public String toIllAdvicePage(Model model){
        List<Info> infoList = infoService.list(new QueryWrapper<Info>().eq("type", "疾病预防建议"));
        model.addAttribute("infoList",infoList);
        return "illAdvice";
    }

    @GetMapping("/healthNote")
    public String toHealthNotePage(Model model){
        List<Info> infoList = infoService.list(new QueryWrapper<Info>().eq("type", "健康生活知识"));
        model.addAttribute("infoList",infoList);
        return "healthNote";
    }

    @GetMapping("/illNote")
    public String toIllNotePage(Model model){
        List<Info> infoList = infoService.list(new QueryWrapper<Info>().eq("type", "疾病预防知识"));
        model.addAttribute("infoList",infoList);
        return "illNote";
    }
}
