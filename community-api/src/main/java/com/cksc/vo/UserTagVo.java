package com.cksc.vo;

import lombok.Data;

import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 19:56
 * @description 用户标签VO类
 */
@Data
public class UserTagVo {
    private List<TagVo> tagVoList;
    private int total;
}
