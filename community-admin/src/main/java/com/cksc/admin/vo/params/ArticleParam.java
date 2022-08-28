package com.cksc.admin.vo.params;


import com.cksc.admin.vo.CategoryVo;
import com.cksc.admin.vo.TagVo;
import lombok.Data;

import java.util.List;

/**
 * 文章参数接收
 */
@Data
public class ArticleParam {

    private Long id; //问题ID

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;

    private Long tid; //团队ID

    private Integer apType;

    private Integer apState;
}
