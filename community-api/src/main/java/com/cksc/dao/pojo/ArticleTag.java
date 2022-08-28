package com.cksc.dao.pojo;

import lombok.Data;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description 文章标签关系类
 */

@Data
public class ArticleTag {

    private Long id;

    private Long articleId;

    private Long tagId;
}
