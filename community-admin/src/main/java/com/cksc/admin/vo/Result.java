package com.cksc.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {

    private boolean success;

    private int code;

    private String message;

    private Object data;


    public static Result success(Object data){
        return new Result(true,20000,"success",data);
    }

    public static Result fail(int code, String message){
        return new Result(false,code,message,null);
    }
}
