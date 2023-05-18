package com.devotion.healthmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devotion.healthmanagement.entity.Body;
import com.devotion.healthmanagement.entity.Health;
import com.devotion.healthmanagement.entity.dto.Msg;
import com.devotion.healthmanagement.service.HealthService;
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
@RequestMapping("/health")
public class HealthController {

    @Autowired
    HealthService healthService;

    @GetMapping("")
    public String toHealthPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        //Page对象
        Page<Health> page = new Page<>(1, 5);
        IPage<Health> iPage = healthService.page(page);
        QueryWrapper<Health> wrapper = new QueryWrapper<>();
        wrapper.eq("id",session.getAttribute("id")).orderByDesc("update_time");
        model.addAttribute("userHealth",healthService.list(wrapper));
        log.info("healthService.list(wrapper)");
        return "health";
    }

    @ResponseBody
    @PostMapping("/update")
    public Msg addHealth(Health userHealth){
        Msg msg = new Msg();
        log.info(userHealth.toString());
        if(userHealth.getId()==null||userHealth.getUpdateTime()==null){
            msg.setInfo("请填写完整信息");
            return msg;
        }
        if(healthService.saveUpdate(userHealth)){
            msg.setInfo("更新成功");
        }else{
            msg.setInfo("更新失败");
        }
        return msg;
    }

    @ResponseBody
    @PostMapping("/delete")
    public Msg deleteHealth(Health userHealth){
        Msg msg = new Msg();
        log.info(userHealth.toString());
        if(healthService.remove(new QueryWrapper<Health>().eq("id",userHealth.getId()).eq("update_time",userHealth.getUpdateTime()))){
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
        List<Health> healths = healthService.list(new QueryWrapper<Health>().like("id",words).orderByDesc("update_time"));
        msg.setInfo("success");
        msg.setData(healths);
        return msg;
    }
}
