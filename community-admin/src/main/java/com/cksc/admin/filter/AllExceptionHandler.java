package com.cksc.admin.filter;


import com.cksc.admin.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//统一异常拦截处理类，对加了@Controller注解的方法进行拦截处理 AOP面向切面的实现
@ControllerAdvice
public class AllExceptionHandler {
    //进行异常处理，处理Exception.class的异常
    @ExceptionHandler(Exception.class)

    @ResponseBody //返回json数据
    public Result doException(Exception e){
        e.printStackTrace();
        return Result.fail(50000,e.getMessage());
    }

}
