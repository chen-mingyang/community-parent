package com.cksc.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jasypt.util.text.BasicTextEncryptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    private static final String jwtToken = "123456Mszlu!@#$$"; //JWT私有秘钥 用JWT私有秘钥和A+B组成C部分签证。

    //创建Token
    public static String createToken(Long userId){
        //B部分playload 存放信息，比如用户id、过期时间，但可被解密，不存敏感信息
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);

        //A部分header 固定{"type":"JWT","alg":"HS256"}
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken) // 签发算法，秘钥为jwtToken 和A+B组成C部分-签证
                .setClaims(claims) // body数据，要唯一，自行设置
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 60 * 1000));// 一天的有效时间

        String token = jwtBuilder.compact(); //生成Token
        return token;
    }

    //检查Token
    public static Map<String, Object> checkToken(String token){
        try {
            //通过密钥 解析Token
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            //拿到B部分
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    ////测试Token
    //public static void main(String[] args) {
    //    //生成Token
    //    String token=JWTUtils.createToken(100L);
    //    System.out.println(token);
    //    //解析Token
    //    Map<String, Object> map = JWTUtils.checkToken(token);
    //    System.out.println(map.get("userId"));
    //}

    //public static void main(String[] args) {
    //    BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
    //    //加密所需的salt
    //    textEncryptor.setPassword("mszlu_blog_$#@wzb_&^%$#");
    //    //要加密的数据（数据库的用户名或密码）
    //    String username = textEncryptor.encrypt("root");
    //    String password = textEncryptor.encrypt("root");
    //    System.out.println("username:"+username);
    //    System.out.println("password:"+password);
    //    System.out.println(textEncryptor.decrypt("29cZ+X9cNmECjbLXT2P/BBZWReVl30NS"));
    //}

}
