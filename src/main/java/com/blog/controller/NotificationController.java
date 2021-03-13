package com.blog.controller;

import com.blog.enums.notificationEnum;
import com.blog.service.CommentService;
import com.blog.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/notification/{action}")
    public String notification(@PathVariable("action")int id,
                               HttpServletRequest request){
        //将通知设置为已读
        notificationService.updateStatus(id);
        //获取type，检验是回复评论还是回复问题
        int type = notificationService.getTypeById(id);
        int questionId;
        if(type == notificationEnum.NOTIFICATION_QUESTION.getType()){
            questionId = notificationService.getOuterIdById(id);
        }else {
            questionId = commentService.getParentIdById(id);
        }
        return "redirect:/question/"+questionId;
    }
}
