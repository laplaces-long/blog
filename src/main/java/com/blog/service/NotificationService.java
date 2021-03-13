package com.blog.service;

import com.blog.dto.NotificationDto;
import com.blog.dto.PageDto;
import com.blog.enums.notificationEnum;
import com.blog.mapper.NotificationMapper;
import com.blog.model.Comment;
import com.blog.model.Notification;
import com.blog.model.User;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    public int getUnreadCount(Integer id) {
        return notificationMapper.getUnreadCount(id);
    }

    public void insertNotification(Notification notification) {
        notificationMapper.insertNotification(notification);
    }

    public void updateStatus(int id) {
        notificationMapper.updateStatus(id);
    }

    public int getTypeById(int id) {
        return notificationMapper.getTypeById(id);
    }

    public int getOuterIdById(int id) {
        return notificationMapper.getOuterIdById(id);
    }

    public PageDto<NotificationDto> list(Integer id, int page, int size) {
        PageDto pageDto = new PageDto();
        int totalcount = notificationMapper.count(id);
        pageDto.setPagination(totalcount, page, size);
        PageHelper.startPage(page, size);
        List<Notification> notifications = notificationMapper.list(id);
        List<NotificationDto> notificationDtoList = new ArrayList<>();

        //将notification插入到notificationDto中，再将user信息也插入到notificationDto中
        //最后插入到notificationDtoList列表里
        for (Notification notification : notifications) {
            User user = userService.findById(notification.getNotifier());
            NotificationDto notificationDto = new NotificationDto();
            BeanUtils.copyProperties(notification, notificationDto);
            notificationDto.setNotifier(user);
            String outercontent;
            if (notification.getType() == notificationEnum.NOTIFICATION_QUESTION.getType()) {
                outercontent = questionService.getTitleById(notification.getOuterid());
                //插入问题的id
                notificationDto.setQuestionid(notification.getOuterid());
            } else {
                outercontent = commentService.getContentById(notification.getOuterid());
                //插入问题的id
                Comment comment=commentService.getParentById(notification.getOuterid());
                notificationDto.setQuestionid(comment.getParentId());
            }
            notificationDto.setOutercontent(outercontent);
            notificationDtoList.add(notificationDto);
        }
        //在pageDto中插入notificationDtoList
        pageDto.setData(notificationDtoList);
        return pageDto;
    }
}
