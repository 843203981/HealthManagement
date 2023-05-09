package com.devotion.healthmanagement.config;

import com.devotion.healthmanagement.entity.User;
import com.devotion.healthmanagement.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Component
public class UserLoginInterceptor implements HandlerInterceptor {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uname = (String) request.getSession().getAttribute("uname");
        if(uname != null){
            redisUtil.expire(uname,1800);
            return true;
        }
        log.info("拦截！");
        response.sendRedirect("/login");
        return false;
    }



}
