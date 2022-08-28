package com.cksc.vo.params;

import com.cksc.vo.CategoryVo;
import com.cksc.vo.TagVo;
import lombok.Data;

import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/14 11:45
 * @description 问题参数
 */
@Data
public class ProblemParam {
    private Long id; //文章ID

    private ProblemBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;

    private Long tid; //团队ID
}
