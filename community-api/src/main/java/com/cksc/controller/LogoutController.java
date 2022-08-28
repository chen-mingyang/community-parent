package com.cksc.controller;

import com.cksc.common.aop.LogAnnotation;
import com.cksc.service.LoginService;
import com.cksc.vo.Result;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logout")
public class LogoutController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    @ApiOperation(value="用户退出",notes = "根据传来的参数")
    @LogAnnotation(module="用户退出",operator="根据传来的参数")
    public Result logout(@RequestHeader("Authorization") String token){
        return loginService.logout(token);
    }
}
