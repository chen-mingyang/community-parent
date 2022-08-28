package com.cksc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.dao.mapper.FocusMapper;
import com.cksc.dao.mapper.SysUserMapper;
import com.cksc.dao.pojo.*;
import com.cksc.service.FocusService;
import com.cksc.vo.*;
import com.cksc.vo.params.FocusParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/15 19:28
 * @description 关注服务实现类
 */

@Service
public class FocusServiceImpl implements FocusService {
    @Autowired
    private FocusMapper focusMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    //Tag 转移到 TagVo
    public FocusVo copy(Focus focus){
        FocusVo focusVo = new FocusVo();
        BeanUtils.copyProperties(focus,focusVo);
        //查询文章
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getId,focus.getFrUid());
        queryWrapper.select(SysUser::getId,SysUser::getNickname);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);


        //这里返回UserVo 里面ID为String类型，所以不丢失精度
        //focusVo.setSysUser(sysUser);
        UserVo userVo = new UserVo();
        //对象复制
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        focusVo.setSysUser(userVo);

        focusVo.setFid(String.valueOf(focus.getFid()));
        return focusVo;
    }
    //List<Tag> 转移到 List<TagVo>
    public List<FocusVo> copyList(List<Focus> tagList){
        List<FocusVo> focusVoList = new ArrayList<>();
        //循环转换对象
        for (Focus focus : tagList) {
            focusVoList.add(copy(focus));
        }
        return focusVoList;
    }

    @Override
    public int findFocusCountsById(Long id) {
        LambdaQueryWrapper<Focus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Focus::getFrUid,id);
        int focusCounts=focusMapper.selectCount(queryWrapper);
        return focusCounts;
    }

    @Override
    public Result getUserFocusList(FocusParam focusParam) {
        UserFocusVo userFocusVo = new UserFocusVo();
        //Mybatis-plus分页
        Page<Focus> page = new Page<>(focusParam.getPage(),focusParam.getPageSize());
        //apType=1 文章
        IPage<Focus> focusIPage = focusMapper.getUserFocusList(
                page,
                focusParam.getFsUid(),
                focusParam.getNickname());
        List<Focus> records = focusIPage.getRecords(); //在分页中得到List<Like>

        userFocusVo.setTotal((int) focusIPage.getTotal());

        List<FocusVo> focusVoList = copyList(records);
        userFocusVo.setFocusVoList(focusVoList);
        //返回VO
        return Result.success(userFocusVo);
    }

    @Override
    public Result deleteUserFocusById(Long fid) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("fid",fid);
        focusMapper.deleteByMap(map);
        return Result.success("删除成功");
    }
}
