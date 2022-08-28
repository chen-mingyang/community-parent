package com.cksc.dao.pojo;

import lombok.Data;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description 文章内容类 与数据库表意一一对应 驼峰原则
 */

@Data
public class ArticleBody {

    private Long id;
    private String content;
    private String contentHtml;
    private Long articleId;
}
