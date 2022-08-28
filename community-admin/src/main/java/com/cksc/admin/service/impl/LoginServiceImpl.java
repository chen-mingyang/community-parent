package com.cksc.admin.service.impl;

import com.alibaba.fastjson.JSON;

import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.service.LoginService;
import com.cksc.admin.service.SysUserService;
import com.cksc.admin.utils.JwtUtils;
import com.cksc.admin.vo.ErrorCode;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional //事务注解 加上保证注册程序出错 有数据回滚，理论上加在service接口处
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;

    //@Autowired
    //private RedisTemplate<String,String> redisTemplate; //整合Redis

    private static final String slat = "mszlu!@#"; //加密盐 可以写死、也可以去数据库获取

    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1. 检查参数是否合法
         * 2. 根据用户名和密码去user表中查询 是否存在
         * 3. 如果不存在 登录失败
         * 4. 如果存在 ，使用jwt 生成token 返回给前端
         * 5. token放入redis当中，redis  token：user信息 设置过期时间
         *  (登录认证的时候 先认证token字符串是否合法，去redis认证是否存在)
         */

        String account = loginParam.getUsername();
        String password = loginParam.getPassword();
        //判断空
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMessage());
        }

        //MD5加密+盐值 针对用户登录输入的密码 加密后和数据库里匹配
        password = DigestUtils.md5Hex(password + slat);

        SysUser sysUser = sysUserService.findUser(account,password);
        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMessage());
        }

        //判断用户授权
        String role;
        if (sysUser.getUserType()==1){
            role="admin";
        }else if (sysUser.getUserType()==2){
            role="member";
        }else{
            role="user";
        }
        //登录成功 创建token 通过用户角色、用户ID、JWT密钥构造token
        String token= JwtUtils.generateToken(role, String.valueOf(sysUser.getId()));
        //登录成功 返回token、role用户角色
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("role",role);

        //存储token、用户信息、设置过期时间
        //redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(hashMap);
    }

    //@Override
    //public SysUser checkToken(String token) {
    //    //检验token是否为空
    //    if (StringUtils.isBlank(token)){
    //        return null;
    //    }
    //    //通过JWTUtils密钥解析token，取出B部分
    //    Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
    //    if (stringObjectMap == null){
    //        return null;
    //    }
    //
    //    //通过token取出redis的用户信息Json
    //    String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
    //    if (StringUtils.isBlank(userJson)){
    //        return null;
    //    }
    //
    //    //从redis中取出用户信息 转换成 SysUser
    //    SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
    //    return sysUser;
    //}

    //@Override
    //public Result logout(String token) {
    //    redisTemplate.delete("TOKEN_"+token); //删除redis中的token
    //    return Result.success(null);
    //}

}
