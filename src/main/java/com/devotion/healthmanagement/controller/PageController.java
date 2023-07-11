package com.devotion.healthmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.*;
import com.devotion.healthmanagement.entity.dto.Msg;
import com.devotion.healthmanagement.entity.dto.UserFood;
import com.devotion.healthmanagement.entity.dto.UserSport;
import com.devotion.healthmanagement.service.*;
import com.devotion.healthmanagement.utils.DateUtil;
import com.devotion.healthmanagement.utils.MatchUtils;
import com.devotion.healthmanagement.utils.RSA;
import com.devotion.healthmanagement.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
    @Autowired
    RedisUtil redisUtil;

    @GetMapping("/index")
    public String toIndexPage(){
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("publicKey",rsa.publicKey);
        return "login";
    }
    @ResponseBody
    @PostMapping("/login")
    public User userLogin(User user, HttpServletResponse response, HttpServletRequest request, Model model) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        log.info("登录"+user);
        if(userService.getOne(new QueryWrapper<User>().eq("uname",user.getUname()))==null){
            User userR = new User();
            userR.setId(0);
            return userR;
        }
        User userR = userService.login(user);
        HttpSession session = request.getSession();
        session.setAttribute("uname",String.valueOf(userR.getUname()));
        session.setAttribute("name",String.valueOf(userR.getName()));
        session.setAttribute("id",String.valueOf(userR.getId()));
        session.setAttribute("isAdmin",String.valueOf(userR.getAdmin()));
        log.info("{id="+session.getAttribute("id")+",uname="+session.getAttribute("uname")+"},session为"+session);
        return userR;
    }

    @GetMapping("/register")
    public String toRegisterPage(){
        return "register";
    }

    @GetMapping("/logout")
    public void LogoutPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String uname = (String) session.getAttribute("uname");
        log.info("用户登出"+uname);
        redisUtil.delete(uname);
        log.info("已清除redis");
        session.removeAttribute("uname");
        session.removeAttribute("name");
        session.removeAttribute("id");
        session.removeAttribute("isAdmin");
        log.info("已清除session");
        response.sendRedirect("/login");
    }

    @ResponseBody
    @PostMapping("/register")
    public Msg register(User user, Model model){
        Msg msg = new Msg();
        log.info("注册用户"+user);
        if(user.getUname()==null||user.getPassword()==null){
            msg.setInfo("用户名或密码不能为空! ");
            return msg;
        }
        if (userService.getOne(new QueryWrapper<User>().eq("uname",user.getUname()))!=null){
            msg.setInfo("用户名已存在! ");
            return msg;
        }
        user.setId(null);
        user.setAdmin(0);
        if(userService.save(user)){
            msg.setInfo("注册成功!");
        }else{
            msg.setInfo("注册失败!");
        }
        return msg;
    }


    /*
    //废弃的功能
    @GetMapping("/todolist")
    public String todolistPage(){
        return "todolist";
    }
    */

    @GetMapping("/userIndex")
    public String toMainPage(Model model, HttpServletRequest request){
        Integer id = Integer.valueOf((String) request.getSession().getAttribute("id"));

        //首页最新数据的日期
        QueryWrapper<Health> healthQueryWrapper = new QueryWrapper<>();
        healthQueryWrapper.eq("id",id).orderByDesc("update_time");
        List<Health> healthList = healthService.list(healthQueryWrapper);
        Health healthLatest = healthList.get(0);

        //今日饮食记录
        Date date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<UserFood> userFoods = foodService.list(id,sdf.format(date));
        UserFood userFoodDay = foodService.getUserFoodDate(userFoods);

        //今日运动记录
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

        return "userIndex";
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
