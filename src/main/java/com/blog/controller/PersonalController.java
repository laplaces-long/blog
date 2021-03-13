package com.blog.controller;


import com.blog.dto.NotificationDto;
import com.blog.dto.PageDto;
import com.blog.model.Question;
import com.blog.model.User;
import com.blog.service.CommentService;
import com.blog.service.NotificationService;
import com.blog.service.QuestionService;
import com.blog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

//个人中心部分
@Controller
public class PersonalController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/personal/{action}")
    public String personal(@PathVariable(name = "action")String action,
                           Model model,
                           HttpServletRequest request,
                           @RequestParam(name = "page",defaultValue = "1")int page,
                           @RequestParam(name = "size",defaultValue = "10")int size){
        Subject subject = SecurityUtils.getSubject();
        User user = null;
        if(subject.isAuthenticated()){
            String name = (String) subject.getPrincipals().getPrimaryPrincipal();
            user = userService.findByName(name);
            request.getSession().setAttribute("user", user);
            int unreadnum = notificationService.getUnreadCount(user.getId());
            request.getSession().setAttribute("unreadnum",unreadnum);
        }

        if (action.equals("questions")){
            model.addAttribute("section","questions");
            model.addAttribute("sectionname","我的问题");
            PageDto<Question> pagination = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", pagination);
        }else if (action.equals("information")){
            model.addAttribute("section","information");
            model.addAttribute("sectionname","我的消息");
            PageDto<NotificationDto> notifications= notificationService.list(user.getId(), page, size);
            model.addAttribute("notifications",notifications);
        }
        return "personal";
    }


}
