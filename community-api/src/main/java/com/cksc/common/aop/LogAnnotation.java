package com.cksc.common.aop;

import java.lang.annotation.*;

/**
 * 开发日志注解annotation
 */

//将Component所有注解中Target、Retention、Documented复制过来
// Type 代表可以放在类上面 Method 代表可以放在方法上
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    //定义参数
    String module() default ""; //模块名称

    String operator() default ""; //操作名称
}
