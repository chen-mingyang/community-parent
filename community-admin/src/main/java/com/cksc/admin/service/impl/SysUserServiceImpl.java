package com.cksc.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.admin.dao.mapper.SysUserMapper;
import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.service.SysUserService;
import com.cksc.admin.vo.PageResult;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.UserVo;
import com.cksc.admin.vo.params.UserParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        UserVo userVo  = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        return userVo;
    }

    @Override
    public SysUser findUser(String account, String password) {

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account); //匹配账号
        queryWrapper.eq(SysUser::getPassword,password); //匹配密码
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname,SysUser::getUserType);
        queryWrapper.last("limit 1"); //加限制 查1条

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public SysUser findUserById(Long userId) {
        return sysUserMapper.selectById(userId);
    }


    @Override
    public Result getUserList(UserParam userParam) {
        PageResult pageResult = new PageResult();
        //Mybatis-plus分页
        Page<SysUser> page = new Page<>(userParam.getPage(),userParam.getPageSize());
        //携带分页数据 查询用户列表
        IPage<SysUser> sysUserIPage = sysUserMapper.getUserList(
                page,
                userParam.getNickname());

        List<SysUser> records = sysUserIPage.getRecords(); //在分页中得到List<TeamUser>

        pageResult.setTotal((int) sysUserIPage.getTotal());

        List<UserVo> userVoList = copyList(records);
        pageResult.setList(userVoList);
        //返回VO
        return Result.success(pageResult);
    }

    @Override
    public Result deleteUserById(Long id) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",id);
        //删除用户发布的文章 问题

        //删除用户创建的标签

        //删除用户发布的评论

        //删除用户创建的团队、加入的团队

        //删除用户的关注、点赞、收藏、黑名单

        //删除用户的投诉、通知

        //删除用户
        sysUserMapper.deleteByMap(map);
        //以上可通过绑定外键级联
        return Result.success("删除成功");
    }

    @Override
    public Result changeUserType(UserParam userParam) {
        SysUser sysUser = new SysUser();
        sysUser.setId(userParam.getId());
        sysUser.setUserType(userParam.getUserType());
        sysUser.setUpdateTime(new Date());
        sysUserMapper.updateById(sysUser);
        return Result.success("改变用户权限成功");
    }

    @Override
    public Result changeUserState(UserParam userParam) {
        SysUser sysUser = new SysUser();
        sysUser.setId(userParam.getId());
        sysUser.setUserState(userParam.getUserState());
        sysUser.setUpdateTime(new Date());
        sysUserMapper.updateById(sysUser);
        return Result.success("改变用户状态成功");
    }


    //List<Article> 转移到 List<ArticleVo> 首页文章列表用
    private List<UserVo> copyList(List<SysUser> records) {
        List<UserVo> UserVoList = new ArrayList<>();
        for (SysUser sysUser: records) {
            UserVoList.add(copy(sysUser));
        }
        return UserVoList;
    }

    //Article对象 转移到 ArticleVo对象
    private UserVo copy(SysUser sysUser){
        UserVo UserVo = new UserVo();
        //对特殊值处理
        UserVo.setId(String.valueOf(sysUser.getId()));
        //对象复制
        BeanUtils.copyProperties(sysUser,UserVo);
        UserVo.setNickname(sysUser.getNickname());
        UserVo.setAvatar(sysUser.getAvatar());
        UserVo.setAccount(sysUser.getAccount());
        UserVo.setBriefly(sysUser.getBriefly());
        UserVo.setEmail(sysUser.getEmail());
        UserVo.setLastLogin(new DateTime(sysUser.getLastLogin()).toString("yyyy-MM-dd HH:mm"));
        UserVo.setPhoneNumber(sysUser.getPhoneNumber());
        UserVo.setQq(sysUser.getQq());
        UserVo.setWechat(sysUser.getWechat());
        UserVo.setBili(sysUser.getBili());
        UserVo.setBlog(sysUser.getBlog());
        UserVo.setUserType(sysUser.getUserType());
        UserVo.setUserState(sysUser.getUserState());
        UserVo.setUpdateTime(new DateTime(sysUser.getUpdateTime()).toString("yyyy-MM-dd HH:mm"));
        UserVo.setCreateDate(new DateTime(sysUser.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        return UserVo;
    }


}
