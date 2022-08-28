package com.cksc.admin.vo.params;

import lombok.Data;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/14 11:46
 * @description 问题内容参数
 */
@Data
public class ProblemBodyParam {
    private String content;

    private String contentHtml;
}
