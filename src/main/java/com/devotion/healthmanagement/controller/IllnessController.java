package com.devotion.healthmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.Illness;
import com.devotion.healthmanagement.entity.dto.Msg;
import com.devotion.healthmanagement.entity.dto.UserIllness;
import com.devotion.healthmanagement.service.IllnessService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequestMapping("/illness")
public class IllnessController {

    @Autowired
    IllnessService illnessService;

    @GetMapping("")
    public String toIllnessPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        List<UserIllness> userIllness = illnessService.getDiseaseByUserId(Integer.valueOf((String) session.getAttribute("id")));
        for (UserIllness illness :
                userIllness
             ) {
            illness.setIllName(illnessService.getById(illness.getIllId()).getIllName());
        }
        model.addAttribute("userIllness", userIllness);
        log.info("illnessService.getDiseaseByUserId((Integer) session.getAttribute(\"id\"))");
        return "illness";
    }

    @ResponseBody
    @PostMapping("/updateUserIllness")
    public Msg setIllnessService(UserIllness userIllness){
        Msg msg = new Msg();
        log.info(userIllness.toString());
        if(illnessService.saveUserIllness(userIllness)){
            msg.setInfo("添加成功");
        }else{
            msg.setInfo("添加失败");
        }
        return msg;
    }


    @ResponseBody
    @RequestMapping("/upload")
    public Msg upload(@RequestParam("file1") MultipartFile file1) throws IOException, InvalidFormatException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        Msg msg = new Msg();
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh_mm_ss");
        String time = ft.format(dNow);

        log.info("收到文件导入："+file1.getOriginalFilename());

        String filePath = "D:/code/HealthManangement/Data";
        File file = new File(filePath+"/"+time+file1.getOriginalFilename()+"data.xlsx");
        file1.transferTo(file);

        illnessService.asyncUpload(file);
        msg.setInfo("上传成功");
        return msg;
    }

    @ResponseBody
    @RequestMapping("/update")
    public Msg update(Illness illness) {

        log.info("更新疾病"+illness);
        Msg msg = new Msg();
        if(illness == null){
            msg.setInfo("疾病不能为空");
        }
        if(illnessService.saveOrUpdate(illness)){
            msg.setInfo("更新成功！");
        }
        else {
            msg.setInfo("更新失败！");
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public Msg delete(Illness illness) {

        log.info("删除疾病"+illness);
        Msg msg = new Msg();
        if(illness.getIllId() == null){
            msg.setInfo("疾病不能为空");
            return msg;
        }
        if(illnessService.removeById(illness.getIllId())){
            msg.setInfo("删除成功！");
        }
        else {
            msg.setInfo("删除失败！");
        }
        return msg;
    }


    @ResponseBody
    @PostMapping("/select")
    public Msg selectIllness(@RequestParam String words){
        log.info("查询疾病"+words);
        Msg msg = new Msg();
        List<Illness> illnesses = illnessService.list(new QueryWrapper<Illness>().like("ill_name",words));
        msg.setInfo("success");
        msg.setData(illnesses);

        return msg;
    }
}
