package com.cksc.controller;


import com.cksc.dao.pojo.SysUser;
import com.cksc.utils.UserThreadLocal;
import com.cksc.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    //@ApiOperation(value="获取本地线程存储用户信息 ",notes = "无条件")
    public Result test(){
        /**
         * 测试在ThreadLocal中获取 用户信息
         */
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
