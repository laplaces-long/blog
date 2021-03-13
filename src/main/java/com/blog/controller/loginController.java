package com.blog.controller;

import com.blog.model.User;
import com.blog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class loginController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login/check")
    public String logincheck(HttpServletRequest request, HttpServletResponse response, Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //当前用户
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        if(!subject.isAuthenticated()){
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);
            try {
                subject.login(usernamePasswordToken);
            } catch (UnknownAccountException e) {
                model.addAttribute("errorMessage","账号错误！");
                return "login";
            }catch (LockedAccountException e) {
                model.addAttribute("errorMessage","账号被锁定！");
                return "login";
            }catch (IncorrectCredentialsException e) {
                model.addAttribute("errorMessage","密码错误");
                return "login";
            }catch (AuthenticationException e) {
                model.addAttribute("errorMessage","认证失败！");
                return "login";
            }
        }
        return "redirect:/index";
    }

    //退出登陆
    @GetMapping("/logout")
    public String logout(){
        Subject subject=SecurityUtils.getSubject();
        //登录当前账号，清空Shiro当前用户的缓存，否则无法重新登录
        subject.logout();
        return "redirect:/index";
    }
}
