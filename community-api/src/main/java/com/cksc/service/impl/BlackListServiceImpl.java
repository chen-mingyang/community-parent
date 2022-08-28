package com.cksc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.dao.mapper.BlackListMapper;
import com.cksc.dao.mapper.SysUserMapper;
import com.cksc.dao.pojo.Blacklist;
import com.cksc.dao.pojo.Focus;
import com.cksc.dao.pojo.SysUser;
import com.cksc.service.BlackListService;
import com.cksc.vo.*;
import com.cksc.vo.params.BlackListParam;
import com.cksc.vo.params.FocusParam;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 22:53
 * @description 黑名单服务实现类
 */
@Service
public class BlackListServiceImpl implements BlackListService {
    @Autowired
    private BlackListMapper blackListMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    //Tag 转移到 TagVo
    public BlackListVo copy(Blacklist blackList){
        BlackListVo blackListVo = new BlackListVo();
        BeanUtils.copyProperties(blackList,blackListVo);

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        //被拉黑对象
        queryWrapper.eq(SysUser::getId,blackList.getBrUid());
        queryWrapper.select(SysUser::getId,SysUser::getNickname);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);


        //这里返回UserVo 里面ID为String类型，所以不丢失精度
        //blackListVo.setSysUser(sysUser);
        UserVo userVo = new UserVo();
        //对象复制
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        blackListVo.setSysUser(userVo);

        blackListVo.setBid(String.valueOf(blackList.getBid()));
        return blackListVo;
    }
    //List<Tag> 转移到 List<TagVo>
    public List<BlackListVo> copyList(List<Blacklist> blacklists){
        List<BlackListVo> blackListVoList = new ArrayList<>();
        //循环转换对象
        for (Blacklist blacklist : blacklists) {
            blackListVoList.add(copy(blacklist));
        }
        return blackListVoList;
    }

    @Override
    public Result getUserBlackList(BlackListParam blackListParam) {
        UserBlackListVo userBlackListVo = new UserBlackListVo();
        //Mybatis-plus分页
        Page<Blacklist> page = new Page<>(blackListParam.getPage(),blackListParam.getPageSize());
        //apType=1 文章
        IPage<Blacklist> blacklistIPage = blackListMapper.getUserBlackList(
                page,
                blackListParam.getBsUid(),
                blackListParam.getNickname());
        List<Blacklist> records = blacklistIPage.getRecords(); //在分页中得到List<Like>

        userBlackListVo.setTotal((int) blacklistIPage.getTotal());

        List<BlackListVo > blackListVoList = copyList(records);
        userBlackListVo.setBlackListVoList(blackListVoList);
        //返回VO
        return Result.success(userBlackListVo);
    }

    @Override
    public Result deleteUserBlackById(Long bid) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("bid",bid);
        blackListMapper.deleteByMap(map);
        return Result.success("删除成功");
    }

    @Override
    public Result queryBlackState(Blacklist blacklist) {
        LambdaQueryWrapper<Blacklist> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blacklist::getBsUid,blacklist.getBsUid());
        queryWrapper.eq(Blacklist::getBrUid,blacklist.getBrUid());
        int count = blackListMapper.selectCount(queryWrapper);
        return Result.success(count);
    }

    @Override
    public Result sendBlackUser(Blacklist blacklist) {
        blacklist.setCreateTime(new Date());
        blackListMapper.insert(blacklist);
        return Result.success("新增成功");
    }
}
