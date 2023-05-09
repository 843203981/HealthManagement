package com.devotion.healthmanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer{

    @Autowired
    UserLoginInterceptor userLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(userLoginInterceptor);
        registration.addPathPatterns("/**"); //所有路径都被拦截
        registration.excludePathPatterns(    //添加不拦截路径
                "/login",                  //登录路径
                "/register",               //注册路径
                "/static/**",              //静态资源
                "/img/**"

        );
    }

    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println();
        registry.addResourceHandler("/static/**").addResourceLocations("file:D:/code/imanagement/manage/manage/src/main/resources/static/");
    }*/
}
