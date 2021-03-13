package com.blog.service;

import com.blog.dto.PageDto;
import com.blog.dto.QuestionDto;
import com.blog.mapper.QuestionMapper;
import com.blog.model.Question;
import com.blog.model.User;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService{
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserService userService;


    public PageDto list(int page, int size) {
        PageDto pageDto = new PageDto();
        //问题的总数量
        int totalcount = questionMapper.count();
        //设置需要显示的页码
        pageDto.setPagination(totalcount, page, size);

        //每页显示5条
        PageHelper.startPage(page, size);
        List<Question> questions = questionMapper.list();
        List<QuestionDto> questionDtoList = new ArrayList<>();

        //根据问题发布人信息显示对应界面
        for (Question question : questions) {
            User user = userService.findById(question.getCreateid());
            QuestionDto questionDto = new QuestionDto();
            //把第一个对象的所有属性拷贝到第二个对象中
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        pageDto.setData(questionDtoList);
        return pageDto;
    }


    public List<Question> getTopTen() {
        return questionMapper.getTopTen();
    }

    public void createQuestion(Question question) {
        questionMapper.createQuestion(question);
    }

    public void updateQuestion(Question question) {
        questionMapper.updateQuestion(question);
    }

    public Question getById(int id) {
        return questionMapper.getById(id);
    }

    public void increaseView(int id) {
        questionMapper.increaseView(id);
    }

    public List<Question> getByTag(int id, String result) {
        return questionMapper.getByTag(id, result);
    }

    public QuestionDto getDtoById(int id) {
        QuestionDto questiondto = new QuestionDto();
        Question question = questionMapper.getById(id);
        BeanUtils.copyProperties(question, questiondto);
        User user = userService.findById(question.getCreateid());
        questiondto.setUser(user);
        return questiondto;
    }

    public void increaseComment(int parentId) {
        questionMapper.increaseComment(parentId);
    }

    public PageDto<Question> list(Integer userid, int page, int size) {
        PageDto pageDto = new PageDto();
        int totalcount = questionMapper.countById(userid);
        pageDto.setPagination(totalcount,page,size);
        //每页只展示size条
        PageHelper.startPage(page, size);
        List<Question> questions = questionMapper.listById(userid);

        List<QuestionDto> questionDtoList = new ArrayList<>();

        for (Question question : questions) {
            User user = userService.findById(question.getCreateid());
            QuestionDto questiondto = new QuestionDto();
            //把第一个对象的所有属性拷贝到第二个对象中
            BeanUtils.copyProperties(question, questiondto);
            questiondto.setUser(user);
            questionDtoList.add(questiondto);
        }
        pageDto.setData(questionDtoList);
        return pageDto;
    }

    public String getTitleById(Integer outerid) {
        return questionMapper.getTitleById(outerid);
    }
}
