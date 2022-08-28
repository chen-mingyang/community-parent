package com.cksc.admin.vo.params;

import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/13 17:37
 * @description 黑名单参数
 */
@Data
public class BlackListParam {

    private int page; //当前页数1

    private int pageSize; //每页显示数量10

    private String nickname;

    private Long bid;

    private Long bsUid;

    private Long brUid;

    private Date createTime;
}
