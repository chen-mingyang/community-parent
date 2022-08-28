package com.cksc.admin.controller;


import com.cksc.admin.common.aop.LogAnnotation;
import com.cksc.admin.common.cache.Cache;
import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.dao.pojo.Tag;
import com.cksc.admin.service.SysUserService;
import com.cksc.admin.service.TagService;
import com.cksc.admin.utils.JwtUtils;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.TagPageParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@PreAuthorize("hasAnyAuthority('admin','member')") //配置角色，拥有该角色的用户方可访问
@RequestMapping("tag")
public class TagController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private TagService tagService;


    /**
     * 获取标签列表分页信息 传递参数
     *
     * @param tagPageParam
     * @return
     */
    @PostMapping("getTagList")
    @ApiOperation(value = "标签列表", notes = "根据表单信息查询")
    @LogAnnotation(module="标签列表",operator="根据表单信息查询")
    //@Cache(expire = 2 * 60 * 1000,name = "adminTagList") //aop缓存redis
    public Result getTagList(@RequestBody TagPageParam tagPageParam) {
        return tagService.getTagList(tagPageParam);
    }

    /**
     * 标签管理 删除标签
     *
     * @param id
     * @return
     */
    @PostMapping("deleteTagById/{id}")
    @ApiOperation(value = "删除标签", notes = "根据问题ID")
    @LogAnnotation(module="删除标签",operator="根据问题ID")
    public Result deleteTagById(@PathVariable("id") Long id) {
        return tagService.deleteTagById(id);
    }

    /**
     * 通过表单信息 更新标签状态
     * @param tag
     * @return
     */
    @PostMapping("changeTagState")
    @ApiOperation(value = "更新标签状态", notes = "通过表单信息")
    @LogAnnotation(module="更新标签状态",operator="通过表单信息")
    public Result changeTagState(@RequestBody Tag tag) {
        return tagService.changeTagState(tag);
    }

    /**
     * 通过表单信息 新增标签
     * @param tag
     * @return
     */
    @PostMapping("addTag")
    @ApiOperation(value = "新增标签", notes = "通过表单信息 ")
    @LogAnnotation(module="新增标签",operator="通过表单信息")
    public Result addTag(@RequestBody Tag tag, HttpServletRequest request) {
        SysUser sysUser=new SysUser();

        //验证前端请求中的token
        Map<String, Object> stringObjectMap = JwtUtils.validateTokenAndGetClaims(request);
        if(stringObjectMap!= null) {
            //获取token中的用户ID 用户角色
            String userid = (String) stringObjectMap.get("userid");
            //根据找到的token，查询用户信息，返回给前端
            sysUser = sysUserService.findUserById(Long.valueOf(userid));
            tag.setUid(sysUser.getId());

            return tagService.addTag(tag);
        }

        return Result.fail(50000,"新增失败");
    }

    /**
     * 通过表单信息 编辑标签
     * @param tag
     * @return
     */
    @PostMapping("updateTag")
    @ApiOperation(value = "编辑标签", notes = "通过表单信息")
    @LogAnnotation(module="编辑标签",operator="通过表单信息")
    public Result updateTag(@RequestBody Tag tag) {
        return tagService.updateTag(tag);
    }

}