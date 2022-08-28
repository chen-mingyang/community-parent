package com.cksc.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description 文章分类类
 */

@Data
public class Category {

    @TableId(type = IdType.AUTO) //数据库自增
    private Long id;

    private String avatar;

    private String categoryName;

    private String description;
}
