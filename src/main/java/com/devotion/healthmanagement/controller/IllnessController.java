package com.devotion.healthmanagement.controller;

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
    @PostMapping("/add")
    public String getIllnessService(UserIllness userIllness){
        log.info(userIllness.toString());
        if(illnessService.saveUserIllness(userIllness)){
            return "{\"msg\":\"添加成功\"}";
        }else{
            return "{\"msg\":\"添加失败,请重试\"}";
        }
    }


    @ResponseBody
    @RequestMapping("/upload")
    public String upload(@RequestParam("file1") MultipartFile file1) throws IOException, InvalidFormatException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh_mm_ss");
        String time = ft.format(dNow);

        log.info("收到文件导入："+file1.getOriginalFilename());

        String filePath = "D:/code/HealthManangement/Data";
        File file = new File(filePath+"/"+time+file1.getOriginalFilename()+"data.xlsx");
        file1.transferTo(file);

        illnessService.asyncUpload(file);
        return "导入成功";
    }
}
