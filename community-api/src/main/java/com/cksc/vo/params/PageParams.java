package com.cksc.vo.params;

import lombok.Data;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description 分页参数VO类
 */

@Data
public class PageParams {

    private int page; //当前页数1

    private int pageSize; //每页显示数量10

    private Long categoryId;

    private Long tagId;

    private Integer apType; //1文章 2问题

    private String title;

    private Long uid; //用户ID

    private Long tid;

    //下列几个参数使用文章归档，点击时间得到对应文章列表
    private String year;

    private String month;

    public String getMonth(){
        if (this.month != null && this.month.length() == 1){
            return "0"+this.month; //加0为了查询条件匹配
        }
        return this.month;
    }
}
