package com.cksc.vo.params;

import com.cksc.dao.pojo.Article;
import com.cksc.dao.pojo.SysUser;
import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 21:10
 * @description 关注接收类
 */
@Data
public class FocusParam {
    private int page; //当前页数1

    private int pageSize; //每页显示数量10

    private String nickname;

    private Long fid;

    private Long fsUid;

    private Long frUid;

    private Date createTime;


}
