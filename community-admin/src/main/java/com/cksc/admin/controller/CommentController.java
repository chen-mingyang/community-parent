package com.cksc.admin.controller;


import com.cksc.admin.common.aop.LogAnnotation;
import com.cksc.admin.common.cache.Cache;
import com.cksc.admin.dao.pojo.Category;
import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.service.CategoryService;
import com.cksc.admin.service.CommentsService;
import com.cksc.admin.service.SysUserService;
import com.cksc.admin.utils.JwtUtils;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.CategoryPageParam;
import com.cksc.admin.vo.params.CommentPageParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@PreAuthorize("hasAnyAuthority('admin','member')") //配置角色，拥有该角色的用户方可访问
@RequestMapping("comment")
public class CommentController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CommentsService commentService;

    /**
     * 获取评论列表分页信息 传递参数
     * @param commentPageParam
     * @return
     */
    @PostMapping("getCommentList")
    @ApiOperation(value = "获取评论列表分页信息", notes = "根据表单信息查询")
    @LogAnnotation(module="获取评论列表分页信息",operator="根据表单信息查询")
    //@Cache(expire = 2 * 60 * 1000,name = "adminCommentList") //aop缓存redis
    public Result getCommentList(@RequestBody CommentPageParam commentPageParam) {
        return commentService.getCommentList(commentPageParam);
    }

    /**
     * 根据评论ID 删除评论
     * @param id
     * @return
     */
    @PostMapping("deleteCommentById/{id}")
    @ApiOperation(value = "删除评论", notes = "根据评论ID")
    @LogAnnotation(module="删除评论",operator="根据评论ID")
    public Result deleteCommentById(@PathVariable("id") Long id) {
        return commentService.deleteCommentById(id);
    }


}