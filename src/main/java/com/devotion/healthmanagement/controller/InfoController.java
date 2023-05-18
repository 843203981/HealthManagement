package com.devotion.healthmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.Info;
import com.devotion.healthmanagement.entity.dto.Msg;
import com.devotion.healthmanagement.service.InfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/info")
public class InfoController {

    @Autowired
    InfoService infoService;

    @ResponseBody
    @PostMapping("/update")
    public Msg updateInfo(Info info){
        log.info("info:"+info);
        infoService.saveOrUpdate(info);
        Msg msg = new Msg();
        msg.setInfo("更新成功");
        return msg;
    }

    @ResponseBody
    @PostMapping("/delete")
    public Msg deleteInfo(Info info){
        log.info("info:"+info);
        Msg msg = new Msg();
        if(infoService.removeById(info.getInfoId())){
            msg.setInfo("删除成功");
        }else {
            msg.setInfo("删除失败");
        }
        return msg;
    }

@ResponseBody
@PostMapping("/select")
    public Msg selectInfo(String words){
        log.info("selectInfo:"+words);
        Msg msg = new Msg();
        msg.setInfo("success");
        msg.setData(infoService.list(new QueryWrapper<Info>().like("info",words)));
        return msg;
    }
}
