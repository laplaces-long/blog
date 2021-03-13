package com.blog.mapper;

import com.blog.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Question record);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKeyWithBLOBs(Question record);

    int updateByPrimaryKey(Question record);

    List<Question> list();

    int count();

    List<Question> getTopTen();

    void createQuestion(Question question);

    void updateQuestion(Question question);

    Question getById(int id);

    void increaseView(int id);

    List<Question> getByTag(@Param("id") int id, @Param("result") String result);

    void increaseComment(int parentId);

    int countById(Integer userid);

    List<Question> listById(Integer userid);

    String getTitleById(Integer outerid);
}