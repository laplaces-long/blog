package com.blog.mapper;

import com.blog.model.Notification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Notification record);

    int insertSelective(Notification record);

    Notification selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Notification record);

    int updateByPrimaryKey(Notification record);

    int getUnreadCount(Integer id);

    void insertNotification(Notification notification);

    void updateStatus(int id);

    int getTypeById(int id);

    int getOuterIdById(int id);

    List<Notification> list(Integer id);

    int count(Integer id);
}