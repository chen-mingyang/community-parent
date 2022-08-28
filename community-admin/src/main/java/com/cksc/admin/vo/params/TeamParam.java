package com.cksc.admin.vo.params;


import com.cksc.admin.dao.pojo.Category;
import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/18 23:05
 * @description 团队参数接收
 */
@Data
public class TeamParam {
    private Long tid;

    //前端给参数时 这里接收参数要小写 tname
    //返回数据时，大写tName 会变成 tname 自动规范转换
    private String tName;

    private String tBriefly;

    private String tHeadshot;

    private String tEmail;

    private Category category;

    private Integer teamState;

    private Date createTime;

    private Date updateTime;

    private Long uid;

}
