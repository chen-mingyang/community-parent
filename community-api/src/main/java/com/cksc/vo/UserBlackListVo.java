package com.cksc.vo;

import lombok.Data;

import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 20:43
 * @description 黑名单分页VO
 */
@Data
public class UserBlackListVo {

    private List<BlackListVo> blackListVoList;

    private int total;

}
