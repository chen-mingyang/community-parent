package com.cksc.admin.controller;

import com.cksc.admin.common.aop.LogAnnotation;
import com.cksc.admin.common.cache.Cache;
import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.service.LoginService;
import com.cksc.admin.service.SysUserService;
import com.cksc.admin.utils.JwtUtils;
import com.cksc.admin.vo.Result;

import com.cksc.admin.vo.params.UserParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/22 17:29
 * @description 用户管理控制类
 */

@RestController
@PreAuthorize("hasAnyAuthority('admin','member')") //配置角色，拥有该角色的用户方可访问
@RequestMapping("user")
public class UserController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value="接收查询用户请求",notes = "验证前端请求携带的Token并返回信息")
    @GetMapping(value = "info")
    @LogAnnotation(module="接收查询用户请求",operator="验证前端请求携带的Token并返回信息")
    public Result userinfo(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("登录后输出用户请求路径"+request.getRequestURL());
        SysUser sysUser=new SysUser();

        //验证前端请求中的token
        Map<String, Object> stringObjectMap = JwtUtils.validateTokenAndGetClaims(request);

        if(stringObjectMap!= null) {
            //获取token中的用户ID 用户角色
            String userid= (String) stringObjectMap.get("userid");

            //根据找到的token，查询用户信息，返回给前端
            sysUser=sysUserService.findUserById(Long.valueOf(userid));

            //Map<String, Object> map = new HashMap<>();
            //List<String> list = new ArrayList<>();
            //list.add("admin");
            //map.put("roles",list);
            //map.put("introduction","I am a super administrator");
            //map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            //map.put("name","super admin");
            //map.put("code", "20000");
            //map.put("message","认证成功123");
            //map.put("user",sysUser);

            return Result.success(sysUser);
        }

        return Result.fail(50000,"用户权限不够");

    }


    /**
     * 通过有无nickname 获取用户管理列表
     * @param userParam
     * @return
     */
    @PostMapping("getUserList")
    @ApiOperation(value="获取用户管理列表",notes = "根据表单信息查询")
    @LogAnnotation(module="获取用户管理列表",operator="根据表单信息查询")
    //@Cache(expire = 5 * 60 * 1000,name = "adminUserList") //aop缓存redis
    public Result getUserList(@RequestBody UserParam userParam){
        return sysUserService.getUserList(userParam);
    }

    /**
     * 通过用户ID 删除用户
     * @param id
     * @return
     */
    @PostMapping("deleteUserById/{id}")
    @ApiOperation(value="删除用户",notes = "通过用户ID")
    @LogAnnotation(module="删除用户",operator="通过用户ID")
    public Result deleteUserById(@PathVariable("id") Long id){
        //int i = 10/0;  //测试统一异常拦截类效果
        return sysUserService.deleteUserById(id);
    }

    /**
     * 通过表单信息 授权用户
     * @param userParam
     * @return
     */
    @PostMapping("changeUserType")
    @ApiOperation(value="授权用户",notes = "通过表单信息 ")
    @LogAnnotation(module="授权用户",operator="通过表单信息")
    public Result changeUserType(@RequestBody UserParam userParam){
        return sysUserService.changeUserType(userParam);
    }

    /**
     * 通过表单信息 改变用户状态
     * @param userParam
     * @return
     */
    @PostMapping("changeUserState")
    @ApiOperation(value="改变用户状态",notes = "通过表单信息 ")
    @LogAnnotation(module="改变用户状态",operator="通过表单信息")
    public Result changeUserState(@RequestBody UserParam userParam){
        return sysUserService.changeUserState(userParam);
    }
}
