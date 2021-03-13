package com.blog.controller;

import com.blog.model.User;
import com.blog.service.UserService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.Transient;
import java.util.UUID;

@Controller
public class registerController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register/check")
    @Transient
    public String registercheck(HttpServletRequest request, HttpServletResponse response, Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User userFind1 = userService.findByName(username);
        //判断数据库是否存在该用户，后期改成redis缓存
        if (userFind1 != null){
            model.addAttribute("errorMessage","用户已存在！");
            return "register";
        }

        //随机生成一个salt用于后端加密
        String salt = UUID.randomUUID().toString();
        User user = new User();
        user.setSalt(salt);
        user.setName(username);
        user.setToken(salt);//token已废弃，暂时不作更改
        //后端MD5再加密一次
        String newPassword = new SimpleHash("MD5", password, salt,1).toHex();
        user.setPassword(newPassword);
        userService.insertUser(user);


        User userFind2 = userService.findByName(username);
        //如果用户注册成功，直接跳转到登录界面
        if (userFind2 != null) {
            return "redirect:/login";
        } else {
            //注册失败
            model.addAttribute("errorMessage","注册失败，请重新注册！");
            return "register";
        }
    }
}
