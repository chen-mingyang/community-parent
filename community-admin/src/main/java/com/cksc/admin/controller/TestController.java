package com.cksc.admin.controller;



import com.cksc.admin.utils.JwtUtils;
import com.cksc.admin.vo.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin

@RestController
@PreAuthorize("hasAuthority('admin')") //配置角色，拥有该角色的用户方可访问
@RequestMapping("manage")
public class TestController {

    /**
     * 无token访问
     * @return
     */
    @RequestMapping("userList")
    public Result test(){
        System.out.println("Spring Security JWT");
        return Result.success(null);
    }

    /**
     * 有token访问 返回受保护资源
     * @return
     * @throws Exception
     */
    @GetMapping("testSecurityResource")
    @ResponseBody
    public String testSecurityResource() throws Exception{
        String userid= JwtUtils.getCurrentUserId();
        String role=JwtUtils.getCurrentRole();
        return "受保护的资源,当前用户的id是"+userid+"当前用户的角色是"+role;
    }

}
