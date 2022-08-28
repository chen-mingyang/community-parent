package com.cksc.admin.vo;

import com.cksc.admin.dao.pojo.Article;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentVo  {
    //防止前端 精度损失 把id转为string
//    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    private UserVo author;

    private String content;

    private List<CommentVo> childrens; //子评论

    private String createDate;

    private Integer level;

    private UserVo toUser;

    private Article article;

    private Date updateTime;
}
