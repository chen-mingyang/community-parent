package com.cksc.vo;

import lombok.Data;

import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/16 14:52
 * @description 用户的文章
 */
@Data
public class UserArticleVo {
    private List<ArticleVo> articleVoList;
    private int total;
}
