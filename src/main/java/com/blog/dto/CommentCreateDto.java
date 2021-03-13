package com.blog.dto;

public class CommentCreateDto {
    private int parentId;
    private int type;
    private String content;

    public int getParentId() {
        return parentId;
    }

    public void setParent_id(int parentId) {
        this.parentId = parentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
