package com.cksc.admin.vo;


import com.cksc.admin.dao.pojo.SysUser;
import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 20:53
 * @description 黑名单VO类
 */
@Data
public class BlackListVo {
    private String bid;

    private String bsUid;

    private SysUser sysUser;

    private Date createTime;
}
