package com.cksc.admin.service;


import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.FocusParam;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/15 18:35
 * @description 关注服务接口
 */
public interface FocusService {
    /**
     * 根据用户ID查询 用户得到的关注数
     * @param id
     * @return
     */
    int findFocusCountsById(Long id);

    /**
     * 个人主页-关注管理-关注列表
     * @param focusParam
     * @return
     */
    Result getUserFocusList(FocusParam focusParam);
    /**
     * 个人主页-关注管理-删除关注
     * @param fid
     * @return
     */
    Result deleteUserFocusById(Long fid);
}
