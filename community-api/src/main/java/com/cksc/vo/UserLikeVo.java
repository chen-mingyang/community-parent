package com.cksc.vo;

import com.cksc.dao.pojo.Like;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 20:43
 * @description 收藏分页VO
 */
@Data
public class UserLikeVo {

    private List<LikeVo> likeList;

    private int total;

}
