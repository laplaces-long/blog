package com.blog.controller;

import com.blog.dto.PageDto;
import com.blog.model.Question;
import com.blog.model.User;
import com.blog.service.NotificationService;
import com.blog.service.QuestionService;
import com.blog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


//管理首页功能
@Controller
public class IndexController {
    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    //首页链接
    @GetMapping(value = "/index")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "size", defaultValue = "10") int size){
        Subject subject = SecurityUtils.getSubject();
        User user = null;
        if(subject.isAuthenticated()){
           String name = (String) subject.getPrincipals().getPrimaryPrincipal();
           user = userService.findByName(name);
           request.getSession().setAttribute("user", user);
           int unreadnum = notificationService.getUnreadCount(user.getId());
           request.getSession().setAttribute("unreadnum",unreadnum);
        }

        PageDto pagination = questionService.list(page, size);
        model.addAttribute("pagination", pagination);

        //获取阅读量最高的十篇问题T
        List<Question> questions= questionService.getTopTen();
        model.addAttribute("topquestion",questions);

        return  "index";
    }
}
