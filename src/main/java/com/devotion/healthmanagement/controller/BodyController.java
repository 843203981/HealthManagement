package com.devotion.healthmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.Body;
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
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/body")
public class BodyController {

    @Autowired
    BodyService bodyService;

    @GetMapping("")
    public String toBodyPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        QueryWrapper<Body> wrapper = new QueryWrapper<>();
        wrapper.eq("id",session.getAttribute("id")).orderByDesc("update_time");
        model.addAttribute("userBody",bodyService.list(wrapper));
        log.info("healthService.list(wrapper)");
        return "body";
    }

    @ResponseBody
    @PostMapping("/add")
    public String addHealth(Body userBody){
        log.info(userBody.toString());
        //判空
        if(userBody.getSex()==null||userBody.getAge()==null||userBody.getHeight()==null||userBody.getWeight()==null){
            return "{\"msg\":\"请填写完整数据\"}";
        }
        if(bodyService.save(userBody)){
            return "{\"msg\":\"添加成功\"}";
        }else{
            return "{\"msg\":\"添加失败,请重试\"}";
        }

    }

    @ResponseBody
    @PostMapping("/intensity")
    public String intensity(String intensity, Model model, HttpServletRequest request){
        Integer id = Integer.valueOf((String) request.getSession().getAttribute("id"));
        Float intensity1 = Float.parseFloat(intensity);
        List<Body> bodies = bodyService.list(new QueryWrapper<Body>().eq("id",id).orderByDesc("update_time"));
        Body userBody = bodies.get(0);
        userBody.setIntensity(intensity1);
        //女性基础代谢率=661+9.6*体重(kg)+1.72*身高(cm)-4.7*年龄
        //男性基础代谢率=67+13.73*体重(kg)+5*身高(cm)-6.9*年龄
        if(userBody.getSex().equals("男")){
            userBody.setHeatCount((int) ((67+13.73*userBody.getWeight()+5*userBody.getHeight()-6.9*userBody.getAge())*(intensity1)));
        }else{
            userBody.setHeatCount((int) ((661+9.6*userBody.getWeight()+1.72*userBody.getHeight()-4.7*userBody.getAge())*(intensity1)));
        }
        userBody.setFatCount((int) (userBody.getHeatCount()*0.3/9));
        userBody.setProteinCount((int) (userBody.getHeatCount()*0.2/4));
        userBody.setCarbonCount((int) (userBody.getHeatCount()*0.5/4));
            bodyService.update(userBody,new QueryWrapper<Body>().eq("id",id).eq("update_time",userBody.getUpdateTime()));
        log.info(userBody.toString());
        return "{\"msg\":\"添加成功\"}";
    }

}
