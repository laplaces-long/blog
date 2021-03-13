package com.blog.service;

import com.blog.mapper.UserMapper;
import com.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findByToken(String token) {
        return userMapper.findByToken(token);
    }

    public User findById(Integer id) {
        return userMapper.findById(id);
    }


    public User selectUser(String name, String password) {
        return userMapper.selectUser(name, password);
    }

    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    public User findByName(String name) {
        return userMapper.findByName(name);
    }
}
