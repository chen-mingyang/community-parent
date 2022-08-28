package com.cksc.common.cache;


import java.lang.annotation.*;

/**
 * 开发缓存注解Cache
 * 把Cache注解加哪个地方，哪个地方就是切点，不改变原有代码
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    long expire() default 1 * 60 * 1000; //缓存过期时间
    String name() default ""; //缓存标识 key  可自定义

}
