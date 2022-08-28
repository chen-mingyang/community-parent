package com.cksc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 9:54
 * @description 返回结果VO类
 */

@Data
@AllArgsConstructor
public class Result {

    private boolean success;

    private int code;

    private String msg;

    private Object data;


    //返回成功
    public static Result success(Object data){
        return new Result(true,200,"success",data);
    }

    //返回失败
    public static Result fail(int code, String msg){
        return new Result(false,code,msg,null);
    }
}

