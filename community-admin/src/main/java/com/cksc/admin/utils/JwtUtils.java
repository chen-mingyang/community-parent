package com.cksc.admin.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/21 19:54
 * @description JWT工具类
 */
public class JwtUtils {
    //private static final Logger logger= LoggerFactory.getLogger(JwtUtils.class);
    public static  final long EXPIRATION_TIME=24 * 60 * 60 * 60 * 1000;// 令牌环有效期一天时间
    public static final String SECRET="abc123456def";//令牌环密钥
    public static final String TOKEN_PREFIX="Bearer";//令牌环头标识
    public static final String HEADER_STRING="X-Token";//配置令牌环在http heads中的键值
    public static final String ROLE="ROLE";//自定义字段-角色字段

    //生成token
    public static String generateToken(String userRole,String userid){
        //B部分playload 存放信息，比如用户id、过期时间，但可被解密，不存敏感信息
        HashMap<String,Object> map=new HashMap<>();
        map.put(ROLE,userRole);  //用户角色
        map.put("userid",userid);  //用户ID

        //A部分header 固定{"type":"JWT","alg":"HS256"}
        String jwt= Jwts.builder()
                .setClaims(map) // body数据，要唯一，自行设置
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME)) // 一天的有效时间
                .signWith(SignatureAlgorithm.HS256,SECRET) // 签发算法，秘钥为jwtToken 和A+B组成C部分-签证
                .compact();

        return TOKEN_PREFIX+" "+jwt;
    }

    ////生成令牌环 传来的有效时间
    //public static String generateToken(String userRole,String userid,long exprationtime){
    //    HashMap<String,Object> map=new HashMap<>();
    //    map.put(ROLE,userRole);
    //    map.put("userid",userid);
    //    String jwt= Jwts.builder()
    //            .setClaims(map)
    //            .setExpiration(new Date(System.currentTimeMillis()+exprationtime))
    //            .signWith(SignatureAlgorithm.HS512,SECRET)
    //            .compact();
    //    return TOKEN_PREFIX+" "+jwt;
    //}

    //校验Token 返回B部分playload 如用户ID、用户角色
    public static Map<String,Object> validateTokenAndGetClaims(HttpServletRequest request){
        //获取请求中的token
        String token=request.getHeader(HEADER_STRING);
        if(token==null){
            throw new TokenValidationException("Missing Token");

        }
        else{
            Map<String,Object> body= Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX,""))
                    .getBody();
            return body;
        }
    }

    //获取token中的登录用户ID 验证token后会失效 信息改变了
    public static String getCurrentUserId() {
        String userid= (String) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        if(userid == null || userid == ""){
            return null;
        }
        else {
            return userid;
        }
    }

    //获取token的登录用户角色 验证token后会失效 信息改变了
    public static String getCurrentRole() {
        String role=null;
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
        }
        if(role == null || role == ""){
            return null;
        }
        else{
            return role;
        }
    }

    static class TokenValidationException extends RuntimeException{
        public TokenValidationException(String msg){
            super(msg);
        }
    }
}
