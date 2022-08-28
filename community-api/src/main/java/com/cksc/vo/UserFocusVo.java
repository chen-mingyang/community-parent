package com.cksc.vo;

import lombok.Data;

import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 20:43
 * @description 关注分页VO
 */
@Data
public class UserFocusVo {

    private List<FocusVo> focusVoList;

    private int total;

}
