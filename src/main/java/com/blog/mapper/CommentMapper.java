package com.blog.mapper;

import com.blog.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> getById(int id);

    void insertComment(Comment comment);

    Comment getParentById(int parentId);

    void increaseCommentCount(int parentId);

    List<Comment> getCommentById(@Param("id") int id, @Param("type") int type);

    int getParentIdById(int id);

    String getContentById(Integer outerid);
}