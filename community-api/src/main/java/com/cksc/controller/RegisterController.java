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

@RestController //返回JSON
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    @ApiOperation(value="用户注册",notes = "根据传来的参数")
    @LogAnnotation(module="用户注册",operator="根据传来的参数")
    public Result register(@RequestBody LoginParam loginParam){
        //sso 单点登录，后期如果把登录注册功能 提出去（单独的服务，可以独立提供接口服务）
        return loginService.register(loginParam);
    }
}
