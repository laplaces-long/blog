package com.blog.controller;

import com.blog.dto.CommentCreateDto;
import com.blog.dto.CommentDto;
import com.blog.dto.ResultDto;
import com.blog.enums.NotificationStatusEnum;
import com.blog.enums.notificationEnum;
import com.blog.model.Comment;
import com.blog.model.Notification;
import com.blog.model.Question;
import com.blog.model.User;
import com.blog.service.CommentService;
import com.blog.service.NotificationService;
import com.blog.service.QuestionService;
import com.blog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "/comment")
    public @ResponseBody Object commentPost(@RequestBody CommentCreateDto commentCreateDto,
                                            HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        User user = null;
        if(subject.isAuthenticated()){
            String name = (String) subject.getPrincipals().getPrimaryPrincipal();
            user = userService.findByName(name);
            request.getSession().setAttribute("user", user);
            int unreadnum = notificationService.getUnreadCount(user.getId());
            request.getSession().setAttribute("unreadnum",unreadnum);
        }

        //把评论插入数据库
        Comment comment=new Comment();
        comment.setParentId(commentCreateDto.getParentId());
        comment.setContent(commentCreateDto.getContent());
        comment.setType(commentCreateDto.getType());
        comment.setCreatetime(System.currentTimeMillis());
        comment.setCommentor(user.getId());
        commentService.insertComment(comment);

        if (commentCreateDto.getType()==2){
            //把回复评论的通知插入数据库
            Notification notification=new Notification();
            notification.setNotifier(comment.getCommentor());
            //回复的评论
            Comment comment2 = commentService.getParentById(commentCreateDto.getParentId());
            notification.setReceiver(comment2.getCommentor());
            notification.setOuterid(commentCreateDto.getParentId());
            notification.setType(notificationEnum.NOTIFICATION_COMMENT.getType());
            notification.setCreatetime(System.currentTimeMillis());
            notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
            notificationService.insertNotification(notification);

            //增加评论数
            commentService.increaseCommentCount(commentCreateDto.getParentId());
        }else {
            //把回复问题的通知插入数据库
            Question question=questionService.getById(commentCreateDto.getParentId());
            Notification notification=new Notification();
            notification.setNotifier(user.getId());
            notification.setReceiver(question.getCreateid());
            notification.setOuterid(commentCreateDto.getParentId());
            notification.setType(notificationEnum.NOTIFICATION_QUESTION.getType());
            notification.setCreatetime(System.currentTimeMillis());
            notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
            notificationService.insertNotification(notification);
            //增加问题回复量
            questionService.increaseComment(commentCreateDto.getParentId());
        }
        ResultDto resultDto=new ResultDto();
        return resultDto.success();
    }


    @GetMapping(value = "/comment/{id}")
    public @ResponseBody ResultDto<List<CommentDto>> comments(@PathVariable(name = "id") int id,
                                                              HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        User user = null;
        if(subject.isAuthenticated()){
            String name = (String) subject.getPrincipals().getPrimaryPrincipal();
            user = userService.findByName(name);
        }

        //查找type=2，即是回复评论的评论
        List<Comment> comments = commentService.getCommentById(id,2);
        List<CommentDto> commentDto=new ArrayList<>();

        //把二级评论和对应的User写进每个CommentDto集合中
        for (Comment comment:comments){
            CommentDto dto=new CommentDto();
            BeanUtils.copyProperties(comment,dto);
            dto.setUser(user);
            commentDto.add(dto);
        }
        ResultDto resultDto=new ResultDto();
        //返回数据给前端
        return resultDto.success(commentDto);
    }

}
