package com.devotion.healthmanagement.controller;

import com.devotion.healthmanagement.entity.User;
import com.devotion.healthmanagement.service.UserService;
import com.devotion.healthmanagement.utils.RSA;
import com.devotion.healthmanagement.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RSA rsa;

    @ResponseBody
    @PostMapping("/login")
    public User userLogin(User user, HttpServletResponse response, HttpServletRequest request, Model model) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        log.info("用户登录"+user);
        User userR = userService.login(user);
        HttpSession session = request.getSession();
        session.setAttribute("uname",String.valueOf(userR.getUname()));
        session.setAttribute("name",String.valueOf(userR.getName()));
        session.setAttribute("id",String.valueOf(userR.getId()));
        log.info("{id="+session.getAttribute("id")+",uname="+session.getAttribute("uname")+"},session为"+session);
        return userR;
    }

    @GetMapping("/logout")
    public void LogoutPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String uname = (String) session.getAttribute("uname");
        log.info("用户登出"+uname);
        redisUtil.delete(uname);
        log.info("已清除redis");
        request.getSession().removeAttribute("uname");
        log.info("已清除session");
        response.sendRedirect("/login");
    }

    @PostMapping("/register")
    public String register(User user, Model model){
        log.info("注册用户"+user);
        user.setId(null);
        user.setAdmin(0);
        if(userService.save(user)){
            model.addAttribute("msg","注册成功！");
        return "login";
        }else{
            model.addAttribute("msg","注册失败! ");
            return "register";
        }
    }

    @PostMapping("/updateUser")
    public void updateUser(User user,Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        user.setId(Integer.valueOf((String) request.getSession().getAttribute("id")));
        log.info("修改用户"+user);
        if(user == null){
            model.addAttribute("msg","用户不能为空");
        }
        if(userService.updateUser(user)){
            model.addAttribute("msg","修改成功");
        }
        else {
            model.addAttribute("msg","修改失败");
        }
        response.sendRedirect("/user");
    }

    @PostMapping("/updatePassword")
    public void updatePassword(User user, HttpServletRequest request, Model model, HttpServletResponse response) throws IOException {
        user.setId(Integer.valueOf((String) request.getSession().getAttribute("id")));
        log.info("修改密码"+user);
        if(user == null){
            model.addAttribute("msg","密码不能为空");
        }
        if(userService.updateUser(user)){
            model.addAttribute("msg","修改成功");
        }
        else {
            model.addAttribute("msg","修改失败");
        }
        response.sendRedirect("/user");
    }
}



