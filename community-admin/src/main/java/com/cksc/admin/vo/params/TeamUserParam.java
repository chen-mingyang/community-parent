package com.cksc.admin.vo.params;

import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/18 23:05
 * @description 团队成员参数接收
 */
@Data
public class TeamUserParam {

    private int page; //当前页数1

    private int pageSize; //每页显示数量10

    private String nickname;

    private Long tuId;

    private Long tid;

    private Long uid;

    private Integer tuType;

    private Integer tuState;

    private Date createTime;

    private Date updateTime;

}
