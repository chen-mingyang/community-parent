package com.cksc.admin.controller;



import com.cksc.admin.common.aop.LogAnnotation;
import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.service.LoginService;
import com.cksc.admin.service.SysUserService;
import com.cksc.admin.utils.JwtUtils;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.LoginParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录类 没有任何限制，无需设置用户角色限制
 * @CrossOrigin 这个标记 跨域请求认可，配置后可以不用
 */

//向@RequestMapping注解处理程序方法添加一个@CrossOrigin注解，以便启用CORS
//（默认情况下，@CrossOrigin允许在@RequestMapping注解中指定的所有源和HTTP方法）
//@CrossOrigin

@RestController
@RequestMapping("public")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private SysUserService sysUserService;


    @PostMapping("login")
    @ApiOperation(value="用户登录",notes = "根据传来的参数")
    @LogAnnotation(module="用户登录",operator="根据传来的参数")
    public Result login(@RequestBody LoginParam loginParam){

        return loginService.login(loginParam);
    }

    @PostMapping(value = "logout")
    @ApiOperation(value="退出登录",notes = "删除后端token")
    @LogAnnotation(module="退出登录",operator="删除后端token")
    public Result userlogout(HttpServletRequest request, HttpServletResponse response){

        return Result.success("success");
    }

}
