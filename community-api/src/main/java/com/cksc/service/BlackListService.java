package com.cksc.service;

import com.cksc.dao.pojo.Blacklist;
import com.cksc.vo.Result;
import com.cksc.vo.params.BlackListParam;
import com.cksc.vo.params.FocusParam;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 22:51
 * @description 黑名单服务接口类
 */
public interface BlackListService {
    /**
     * 个人主页-黑名单管理-黑名单列表
     * @param blackListParam
     * @return
     */
    Result getUserBlackList(BlackListParam blackListParam);
    /**
     * 个人主页-黑名单管理-删除黑名单
     * @param bid
     * @return
     */
    Result deleteUserBlackById(Long bid);
    /**
     * 通过发起id、被拉黑id  查询浏览者对文章作者的拉黑屏蔽状态
     * @param blacklist
     * @return
     */
    Result queryBlackState(Blacklist blacklist);
    /**
     * 通过发起id、被拉黑id  添加拉黑信息
     * @param blacklist
     * @return
     */
    Result sendBlackUser(Blacklist blacklist);
}
