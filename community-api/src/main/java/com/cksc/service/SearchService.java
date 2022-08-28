package com.cksc.service;

import com.cksc.vo.Result;
import com.cksc.vo.params.PageParams;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/20 17:25
 * @description 搜索服务接口类
 */
public interface SearchService {
    /**
     * 主页-搜索服务
     * @param pageParams
     * @return
     */
    Result getSearchIndexList(PageParams pageParams);
}
