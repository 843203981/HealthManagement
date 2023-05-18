package com.devotion.healthmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.Body;
import com.devotion.healthmanagement.entity.Food;
import com.devotion.healthmanagement.entity.dto.Msg;
import com.devotion.healthmanagement.service.BodyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/update")
    public Msg addHealth(Body userBody){
        Msg msg = new Msg();
        log.info(userBody.toString());
        //判空
        if(userBody.getSex()==null||userBody.getAge()==null||userBody.getHeight()==null||userBody.getWeight()==null){
            msg.setInfo("请填写完整信息");
            return msg;
        }
        if(bodyService.saveUpdate(userBody)){
            msg.setInfo("更新成功");
        }else{
            msg.setInfo("更新失败");
        }
        return msg;
    }

    @ResponseBody
    @PostMapping("/delete")
    public Msg deleteHealth(Body userBody){
        Msg msg = new Msg();
        log.info(userBody.toString());
        if(bodyService.remove(new QueryWrapper<>(userBody).eq("id",userBody.getId()).eq("update_time",userBody.getUpdateTime()))){
            msg.setInfo("删除成功");
        }else{
            msg.setInfo("删除失败");
        }
        return msg;
    }

    @ResponseBody
    @PostMapping("/select")
    public Msg selectHealth(@RequestParam String words){
        Msg msg = new Msg();
        List<Body> bodies = bodyService.list(new QueryWrapper<Body>().like("id",words).orderByDesc("update_time"));
        msg.setInfo("success");
        msg.setData(bodies);
        return msg;
    }

    @ResponseBody
    @PostMapping("/intensity")
    public Msg intensity(String intensity, Model model, HttpServletRequest request){
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
        Msg msg = new Msg();
        msg.setInfo("更新成功");
        return msg;
    }

}
