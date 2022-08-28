package com.cksc.service.impl;

import com.alibaba.fastjson.JSON;
import com.cksc.dao.pojo.SysUser;
import com.cksc.service.LoginService;

import com.cksc.service.SysUserService;
import com.cksc.utils.JWTUtils;
import com.cksc.vo.ErrorCode;
import com.cksc.vo.Result;
import com.cksc.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional //事务注解 加上保证注册程序出错 有数据回滚，理论上加在service接口处
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate; //整合Redis

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
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        //判断空
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

        //MD5加密+盐值 针对用户登录输入的密码
        password = DigestUtils.md5Hex(password + slat);

        SysUser sysUser = sysUserService.findUser(account,password);
        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        if(sysUser.getUserState()==3){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),"账号已被封禁！");
        }
        //创建token
        String token = JWTUtils.createToken(sysUser.getId());
        //存储token、用户信息、设置过期时间
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        //检验token是否为空
        if (StringUtils.isBlank(token)){
            return null;
        }
        //通过JWTUtils密钥解析token，取出B部分
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null){
            return null;
        }

        //通过token取出redis的用户信息Json
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)){
            return null;
        }

        //从redis中取出用户信息 转换成 SysUser
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token); //删除redis中的token
        return Result.success(null);
    }

    @Override
    public Result register(LoginParam loginParam) {
        /**
         * 1. 判断参数 是否合法
         * 2. 判断账户是否存在，存在 返回账户已经被注册
         * 3. 不存在，注册用户
         * 4. 生成token
         * 5. 存入redis 并返回
         * 6. 注意 加上事务，一旦中间的任何过程出现问题，注册的用户 需要回滚
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)
        ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //根据账户查询用户信息
        SysUser sysUser =  sysUserService.findUserByAccount(account);
        if (sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),"账户已经被注册了");
        }

        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat)); //盐加密
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        //sysUser.setAdmin(1); //1 为true
        //sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setEmail("");
        sysUser.setUserType(3); //用户类型-普通
        sysUser.setUserState(1); //用户状态-正常

        this.sysUserService.save(sysUser);
        //生成token
        String token = JWTUtils.createToken(sysUser.getId());
        //将token存到redis
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

}
