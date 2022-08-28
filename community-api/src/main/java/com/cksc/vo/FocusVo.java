package com.cksc.vo;

import com.cksc.dao.pojo.Article;
import com.cksc.dao.pojo.SysUser;
import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 20:53
 * @description 关注VO类
 */
@Data
public class FocusVo {
    private String fid;

    private String fsUid;

    private UserVo sysUser;

    private Date createTime;
}
