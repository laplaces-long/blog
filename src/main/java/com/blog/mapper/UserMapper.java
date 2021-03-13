package com.blog.mapper;

import com.blog.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User findByToken(String token);

    User findById(Integer id);

    User selectUser(@Param("name") String name, @Param("password") String password);

    void insertUser(User user);

    User findByName(String username);
}