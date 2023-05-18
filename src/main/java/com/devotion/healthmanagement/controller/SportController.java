package com.devotion.healthmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.Sport;
import com.devotion.healthmanagement.entity.dto.Msg;
import com.devotion.healthmanagement.service.SportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/sport")
public class SportController {
    @Autowired
    SportService sportService;

    @ResponseBody
    @PostMapping("/update")
    public Msg updateSport(Sport sport){
        log.info("更新运动"+sport);
        Msg msg = new Msg();
        if(sport == null){
            msg.setInfo("运动不能为空");
        }
        if(sportService.saveOrUpdate(sport)){
            msg.setInfo("更新成功！");
        }
        else {
            msg.setInfo("更新失败！");
        }
        return msg;
    }

    @ResponseBody
    @PostMapping("/delete")
    public Msg deleteSport(Sport sport){
        log.info("删除运动"+sport);
        Msg msg = new Msg();
        if(sport.getSportId() == null){
            msg.setInfo("运动不能为空");
            return msg;
        }
        if(sportService.removeById(sport.getSportId())){
            msg.setInfo("删除成功！");
        }
        else {
            msg.setInfo("删除失败！");
        }
        return msg;
    }

    @ResponseBody
    @PostMapping("/select")
    public Msg selectSport(@RequestParam String words){
        log.info("查询运动"+words);
        Msg msg = new Msg();
        List<Sport> sports = sportService.list(new QueryWrapper<Sport>().like("sport_name",words));
        msg.setInfo("success");
        msg.setData(sports);

        return msg;
    }
}
