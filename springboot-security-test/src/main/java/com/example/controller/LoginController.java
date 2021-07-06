package com.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LoginController {
    @GetMapping("/tologin")
    public String toLogin() {
        return "/login/login";
    }
    @GetMapping("/detail/{type}/{path}")
    public String toDetail(@PathVariable("type") String type,
                           @PathVariable("path") String path){
        return "detail/"+type+"/"+path;
    }
    @GetMapping("/getUserByContext")
    public void getUser(){
        //获取应用上下文
        SecurityContext context = SecurityContextHolder.getContext();
        System.out.println("userDetails=="+context);
        //获取用户相关信息
        Authentication auth = context.getAuthentication();
        System.out.println("Principal: "+auth.getPrincipal());
        System.out.println("Authorities: "+auth.getAuthorities());
    }
}
