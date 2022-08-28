package com.cksc.dao.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description 文章类
 */

@Data
public class Article {

    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    private Long id;
    //原来这些参数是int基本类型，有BUG，
    //因为使用了mybatis plus会给默认值，它会给默认值0，现在改成Integer

    private Integer commentCounts;

    private String summary;

    private String title;

    private Integer viewCounts;
    /**
     * 置顶
     */
    private Integer weight;
    /**
     * 作者用户id
     */
    private Long authorId;
    /**
     * 内容id
     */
    private Long bodyId;
    /**
     *类别id
     */
    private Long categoryId;
    /**
     * 创建时间
     */
    private Long createDate;

    private Date updateTime;

    private Integer apType;

    private Integer apState;

    private Integer openState;

    private Integer commentState;
    /**
     * 团队ID
     */
    private Long tid;
    /**
     * 点赞数量
     */
    private Integer upCounts;
    /**
     * 收藏数量
     */
    private Integer likeCounts;
}
