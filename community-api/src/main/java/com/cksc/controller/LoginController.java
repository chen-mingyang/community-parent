package com.cksc.controller;


import com.cksc.common.aop.LogAnnotation;
import com.cksc.service.LoginService;
import com.cksc.vo.Result;
import com.cksc.vo.params.LoginParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {
//    @Autowired
//    private SysUserService sysUserService; //这样不好、应该再具体下


    @Autowired
    private LoginService loginService; //登录Service组合相关操作

    @PostMapping
    @ApiOperation(value="用户登录",notes = "根据传来的参数")
    @LogAnnotation(module="用户登录",operator="根据传来的参数")
    public Result login(@RequestBody LoginParam loginParam){
        //登录 验证用户  访问用户表，但是
        return loginService.login(loginParam);
    }
}
