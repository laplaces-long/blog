package com.blog.cache;


import com.blog.dto.TagDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagCache {
    public List<TagDto> getTags(){
        List<TagDto> tags=new ArrayList<>();

        TagDto language=new TagDto();
        language.setCategoryname("开发语言");
        language.setTags(Arrays.asList("java","c","c++","php","html","html5","css"));
        tags.add(language);

        TagDto tools=new TagDto();
        tools.setCategoryname("开发工具");
        tools.setTags(Arrays.asList("Spring","SpringBoot","SpringMVC"));
        tags.add(tools);

        return tags;
    }
}
