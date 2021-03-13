package com.blog.service;

import com.blog.dto.CommentDto;
import com.blog.mapper.CommentMapper;
import com.blog.model.Comment;
import com.blog.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

    public List<CommentDto> getById(int id) {
        //通过文章id找到所有回复
        List<Comment> comments = commentMapper.getById(id);
        //创建要给CommentDto的list
        List<CommentDto> lists = new ArrayList<>();
        //遍历每个Comment
        for(Comment comment:comments){
            //找到回复人
            User user = userService.findById(comment.getCommentor());
            CommentDto commentDto = new CommentDto();
            //将第一个元素复制到第二个元素中
            BeanUtils.copyProperties(comment,commentDto);
            commentDto.setUser(user);
            lists.add(commentDto);
        }
        return lists;
    }

    public void insertComment(Comment comment) {
        commentMapper.insertComment(comment);
    }

    public Comment getParentById(int parentId) {
        return commentMapper.getParentById(parentId);
    }

    public void increaseCommentCount(int parentId) {
        commentMapper.increaseCommentCount(parentId);
    }

    public List<Comment> getCommentById(int id, int type) {
        return commentMapper.getCommentById(id, type);
    }

    public int getParentIdById(int id) {
        return commentMapper.getParentIdById(id);
    }

    public String getContentById(Integer outerid) {
        return commentMapper.getContentById(outerid);
    }
}
