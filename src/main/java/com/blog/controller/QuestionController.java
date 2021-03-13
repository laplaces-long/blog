package com.blog.controller;


import com.blog.dto.CommentDto;
import com.blog.dto.QuestionDto;
import com.blog.model.Question;
import com.blog.model.User;
import com.blog.service.CommentService;
import com.blog.service.NotificationService;
import com.blog.service.QuestionService;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")int id,
                           Model model,
                           HttpServletRequest request){
        //查找cookies，观察是否有token存在
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "login";
        }
        User user = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();
                user = userService.findByToken(token);
                if (user != null) {
                    request.getSession().setAttribute("user", user);
                    //获取未读的消息数量
                    int unreadnum = notificationService.getUnreadCount(user.getId());
                    request.getSession().setAttribute("unreadnum",unreadnum);
                }
                break;
            }
        }
        QuestionDto questiondto = questionService.getDtoById(id);
        //增加阅读数
        questionService.increaseView(id);
        model.addAttribute("questionDto",questiondto);
        //展示回复数据
        List<CommentDto> comments = commentService.getById(id);
        model.addAttribute("comments",comments);
        //相关问题
        //分割tag
        String[] tags = questiondto.getTag().split(",");

        //构建正则匹配表达式，用于判断问题是否有相关tag
        StringBuilder msg = new StringBuilder();
        for (String tag: tags){
            msg.append(tag);
            msg.append("|");
        }
        String result = msg.substring(0, msg.length()-1);
        List<Question> relativeQuestion = questionService.getByTag(id, result);
        model.addAttribute("relativequestion",relativeQuestion);

        return "question";
    }
}
