package com.cksc.vo;

import com.cksc.dao.pojo.Article;
import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 20:53
 * @description 收藏VO类
 */
@Data
public class LikeVo {
    private String liId;

    private Article article;

    private String uid;

    private Date createTime;
}
