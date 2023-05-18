package com.devotion.healthmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.User;
import com.devotion.healthmanagement.entity.dto.Msg;
import com.devotion.healthmanagement.service.UserService;
import com.devotion.healthmanagement.utils.RSA;
import com.devotion.healthmanagement.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RSA rsa;

    @GetMapping("")
    public String toUser(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        model.addAttribute("user",userService.getById((Serializable) session.getAttribute("id")));
        return "user";
    }

    @ResponseBody
    @PostMapping("/update")
    public Msg updateUser(User user,Model model, HttpServletResponse response) throws IOException {
        log.info("修改用户"+user);
        Msg msg = new Msg();
        if(user == null){
            msg.setInfo("用户不能为空");
        }
        if(userService.saveOrUpdate(user)){
            msg.setInfo("修改成功！");
        }
        else {
            msg.setInfo("修改失败！");
        }
        return msg;
    }

    @ResponseBody
    @PostMapping("/password")
    public Msg updatePassword(User user, HttpServletRequest request, Model model, HttpServletResponse response) throws IOException {
        Msg msg = new Msg();
        if(user.getPassword()==null){
            msg.setInfo("密码不能为空");
        }
        user.setId(Integer.valueOf((String) request.getSession().getAttribute("id")));
        log.info("修改密码"+user);
        if(userService.updateUser(user)){
            msg.setInfo("修改成功！");
        }
        else {
            msg.setInfo("修改失败！");
        }
        return msg;
    }

    @ResponseBody
    @PostMapping("/delete")
    public Msg deleteUser(User user){
        Msg msg = new Msg();
        log.info("删除用户"+user);
        if(userService.removeById(user.getId())){
            msg.setInfo("删除成功！");
        }
        else {
            msg.setInfo("删除失败！");
        }
        return msg;
    }

    @ResponseBody
    @PostMapping("/select")
    public Msg selectUser(@RequestParam String words,Model model){
        log.info("查询用户"+words);
        Msg msg = new Msg();
        msg.setInfo("success");
        msg.setData(userService.list(new QueryWrapper<User>().like("name",words)));
        return msg;
    }
}



