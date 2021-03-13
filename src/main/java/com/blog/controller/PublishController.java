package com.blog.controller;

import com.blog.cache.TagCache;
import com.blog.dto.TagDto;
import com.blog.model.Question;
import com.blog.model.User;
import com.blog.service.QuestionService;
import com.blog.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PublishController {
    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @GetMapping(value = "/publish")
    public String publish(Model model){
        //发布问题时可用标签组
        TagCache tagCache=new TagCache();
        List<TagDto> tags = tagCache.getTags();
        model.addAttribute("tags",tags);
        return "publish";
    }

    //发布问题
    @PostMapping("/publish")
    public String publishquestion(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam(value = "id",defaultValue = "-1")int id,
            HttpServletRequest request,
            Model model){
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        //标签组
        TagCache tagCache=new TagCache();
        List<TagDto> tags = tagCache.getTags();
        model.addAttribute("tags",tags);
        //后台验证，防止输入的问题为空
        if (StringUtils.isBlank(title)) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (StringUtils.length(title) > 50) {
            model.addAttribute("error", "标题最多 50 个字符");
            return "publish";
        }
        if (StringUtils.isBlank(description)) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (StringUtils.isBlank(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        //获取当前登陆用户的信息
        User user = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();
                user = userService.findByToken(token);
            }
        }
        //将问题上传到数据库
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreateid(user.getId());
        question.setCreatetime(System.currentTimeMillis());
        if(id==-1){
            questionService.createQuestion(question);
        }else {
            question.setId(id);
            questionService.updateQuestion(question);
        }
        return "redirect:/index";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id")int id,
                       Model model){
        Question question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        //用来标识问题是修改而不是重新创建
        model.addAttribute("id",question.getId());
        return "publish";
    }
}
