package com.cksc.admin.vo;


import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.dao.pojo.Team;
import lombok.Data;

import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 9:54
 * @description 文章VO类，VO表示和页面交互的数据，与文章实体类分开可随意定义。
 */

@Data
public class ArticleVo {
//    //序列化 防止精度丢失
//    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    private Integer commentCounts;

    private String summary;

    private String title;

    private Integer viewCounts;

    private Integer weight;
    /**
     * 用户ID
     */
    private SysUser sysUser;
    /**
     * 创建时间
     */
    private ArticleBodyVo body;

    private CategoryVo category;

    private String createDate;

    private String updateTime;

    private Integer apType;

    private Integer apState;

    private Integer openState;

    private Integer commentState;

    //团队名称
    private Team team;

    private Integer upCounts;

    private Integer likeCounts;

    private List<TagVo> tags;



}
