package com.cksc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cksc.dao.mapper.ArticleMapper;
import com.cksc.dao.mapper.FocusMapper;
import com.cksc.dao.mapper.SysUserMapper;
import com.cksc.dao.pojo.Article;
import com.cksc.dao.pojo.Focus;
import com.cksc.dao.pojo.SysUser;
import com.cksc.service.*;

import com.cksc.vo.*;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private FocusMapper focusMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private LoginService loginService;

    @Autowired
    private LikeService likeService;
    @Autowired
    private ThumbsUpService thumbsUpServicel;
    @Autowired
    private FocusService focusService;


    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("社区");
        }
        UserVo userVo  = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        return userVo;
    }

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("社区");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account); //匹配账号
        queryWrapper.eq(SysUser::getPassword,password); //匹配密码
        //queryWrapper.eq(SysUser::getUserState,1); //是否封禁
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname,SysUser::getUserState);
        queryWrapper.last("limit 1"); //加限制 查1条

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1. token合法性校验
         *    是否为空，解析是否成功 redis是否存在
         * 2. 如果校验失败 返回错误
         * 3. 如果成功，返回对应的结果 LoginUserVo
         */
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        //登录用户信息VO
        LoginUserVo loginUserVo = new LoginUserVo();
        //对特殊值处理
        loginUserVo.setId(String.valueOf(sysUser.getId()));
        //对象复制
        BeanUtils.copyProperties(sysUser,loginUserVo);
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setAccount(sysUser.getAccount());

        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        //保存用户这 id会自动生成
        //这个地方 默认生成的id是 分布式id 雪花算法
        //mybatis-plus
        this.sysUserMapper.insert(sysUser);
    }

    @Override
    public Result getUserById(Long id) {
        //根据ID查询 被收藏数-星星数 获得赞数 关注数
        int likeCounts=likeService.findLikeCountsById(id);
        int upCounts = thumbsUpServicel.findThumbsUpCountsById(id);
        int focusCounts = focusService.findFocusCountsById(id);
        //根据ID查询用户信息，加入上面数据到VO中返回
        SysUser sysUser=sysUserMapper.selectById(id);
        UserVo userVo=copy(sysUser);
        userVo.setLikeCounts(likeCounts);
        userVo.setUpCounts(upCounts);
        userVo.setFocusCounts(focusCounts);
        return Result.success(userVo);
    }

    @Override
    public Result updateUser(SysUser sysUser) {
        //LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        //updateWrapper.eq(SysUser::getId,sysUser.getId());
        //sysUserMapper.update(sysUser,updateWrapper);
        sysUser.setUpdateTime(new Date());
        sysUserMapper.updateById(sysUser);
        return Result.success("更新成功");
    }

    @Override
    public Result getUserByApId(Long id) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId,id);
        queryWrapper.select(Article::getAuthorId);
        Article article = articleMapper.selectOne(queryWrapper);

        //根据ID查询 被收藏数-星星数 获得赞数 关注数
        int likeCounts=likeService.findLikeCountsById(article.getAuthorId());
        int upCounts = thumbsUpServicel.findThumbsUpCountsById(article.getAuthorId());
        int focusCounts = focusService.findFocusCountsById(article.getAuthorId());
        //根据ID查询用户信息，加入上面数据到VO中返回
        SysUser sysUser =sysUserMapper.selectById(article.getAuthorId());
        UserVo userVo=copy(sysUser);
        userVo.setLikeCounts(likeCounts);
        userVo.setUpCounts(upCounts);
        userVo.setFocusCounts(focusCounts);
        return Result.success(userVo);
    }

    @Override
    public Result queryFocusState(Focus focus) {
        LambdaQueryWrapper<Focus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Focus::getFsUid,focus.getFsUid());
        queryWrapper.eq(Focus::getFrUid,focus.getFrUid());
        Focus focus1 = focusMapper.selectOne(queryWrapper);
        return Result.success(focus1);
    }

    @Override
    public Result addFocusState(Focus focus) {
        focus.setCreateTime(new Date());
        focusMapper.insert(focus);
        return Result.success("新增成功");
    }

    @Override
    public Result deleteFocusUser(Focus focus) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("fs_uid",focus.getFsUid());
        map.put("fr_uid",focus.getFrUid());
        focusMapper.deleteByMap(map);
        return Result.success("删除成功");
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
