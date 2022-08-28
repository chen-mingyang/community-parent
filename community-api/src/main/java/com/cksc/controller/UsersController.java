package com.cksc.controller;


import com.cksc.common.aop.LogAnnotation;
import com.cksc.dao.pojo.Focus;
import com.cksc.dao.pojo.SysUser;
import com.cksc.dao.pojo.Tag;
import com.cksc.dao.pojo.TeamUser;
import com.cksc.service.*;
import com.cksc.vo.LikeVo;
import com.cksc.vo.Result;
import com.cksc.vo.UserVo;
import com.cksc.vo.params.*;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UsersController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private TagService tagService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FocusService focusService;

    @Autowired
    private BlackListService blackListService;

    @Autowired
    private TeamUserService teamUserService;

    @Autowired
    private TeamService teamService;

    // 获取登录后的用户信息，/users/currentUser
    @GetMapping("currentUser")
    @ApiOperation(value="获取登录后的用户信息 ",notes = "根据头部的token")
    @LogAnnotation(module="获取登录后的用户信息",operator="根据头部的token")
    public Result currentUser(@RequestHeader("Authorization") String token){
        return sysUserService.findUserByToken(token);
    }

    /**
     * --------------个人主页 用户信息板块-------------
     */

    // 通过id 获取当前的用户信息
    @GetMapping("getUserById/{id}")
    @ApiOperation(value="获取当前的用户信息",notes = "根据用户ID")
    @LogAnnotation(module="获取当前的用户信息",operator="根据用户ID")
    public Result getUserById(@PathVariable("id") Long id){
        return sysUserService.getUserById(id);
    }

    // 通过传来的用户信息表单 更新用户信息
    @PostMapping("userEdit")
    @ApiOperation(value="更新用户信息",notes = "根据表单")
    @LogAnnotation(module="更新用户信息",operator="根据表单")
    public Result userEdit(@RequestBody UserParam userParam){
        SysUser sysUser=new SysUser();
        //对特殊值处理
        sysUser.setId(Long.valueOf(userParam.getId()));
        sysUser.setNickname(userParam.getNickname());
        sysUser.setAvatar(userParam.getAvatar());
        sysUser.setAccount(userParam.getAccount());
        sysUser.setBriefly(userParam.getBriefly());
        sysUser.setEmail(userParam.getEmail());
        sysUser.setPhoneNumber(userParam.getPhoneNumber());
        sysUser.setQq(userParam.getQq());
        sysUser.setWechat(userParam.getWechat());
        sysUser.setBili(userParam.getBili());
        sysUser.setBlog(userParam.getBlog());
        sysUser.setUserType(userParam.getUserType());
        sysUser.setUserState(userParam.getUserState());
        System.out.println("______________"+sysUser.getId());
        return sysUserService.updateUser(sysUser);
    }

    /**
     * 个人主页-文章管理 文章列表 /users/getUserArticleList
     * @param pageParams
     * @return
     */
    @PostMapping("getUserArticleList")
    @ApiOperation(value="个人主页-文章管理 文章列表",notes = "根据表单信息查询")
    @LogAnnotation(module="个人主页-文章管理 文章列表",operator="根据表单信息查询")
    public Result getUserArticleList(@RequestBody PageParams pageParams){
        //int i = 10/0;  //测试统一异常拦截类效果
        return articleService.getUserArticleList(pageParams);
    }

    /**
     * 个人主页-文章管理 删除文章
     * @param id
     * @return
     */
    @PostMapping("deleteUserArticleById/{id}")
    @ApiOperation(value="个人主页-文章管理 删除文章",notes = "根据文章ID")
    @LogAnnotation(module="个人主页-文章管理 删除文章",operator="根据文章ID")
    public Result deleteUserArticleById(@PathVariable("id") Long id){
        //int i = 10/0;  //测试统一异常拦截类效果
        return articleService.deleteUserArticleById(id);
    }

    /**
     * 个人主页-问题管理 问题列表 /users/getUserProblemList
     * @param pageParams
     * @return
     */
    @PostMapping("getUserProblemList")
    @ApiOperation(value="个人主页-问题管理 问题列表",notes = "根据表单信息查询")
    @LogAnnotation(module="个人主页-问题管理 问题列表",operator="根据表单信息查询")
    public Result getUserProblemList(@RequestBody PageParams pageParams){
        //int i = 10/0;  //测试统一异常拦截类效果
        return problemService.getUserProblemList(pageParams);
    }

    /**
     * 个人主页-问题管理 删除问题
     * @param id
     * @return
     */
    @PostMapping("deleteUserProblemById/{id}")
    @ApiOperation(value="个人主页-问题管理 删除问题",notes = "根据问题ID")
    @LogAnnotation(module="个人主页-问题管理 删除问题",operator="根据问题ID")
    public Result deleteUserProblemById(@PathVariable("id") Long id){
        //int i = 10/0;  //测试统一异常拦截类效果
        return problemService.deleteUserProblemById(id);
    }

    /**
     * --------------个人主页 标签板块-------------
     */

    /**
     * 获取标签列表分页信息 传递参数
     * @param tagPageParam
     * @return
     */
    @PostMapping("getUserTagList")
    @ApiOperation(value="个人主页标签列表",notes = "根据表单信息查询")
    @LogAnnotation(module="个人主页标签列表",operator="根据表单信息查询")
    public Result getUserTagList(@RequestBody TagPageParam tagPageParam){
        //int i = 10/0;  //测试统一异常拦截类效果
        return tagService.getUserTagList(tagPageParam);
    }

    /**
     * 个人主页-标签管理 删除标签
     * @param id
     * @return
     */
    @PostMapping("deleteUserTagById/{id}")
    @ApiOperation(value="个人主页标签管理删除标签",notes = "根据问题ID")
    @LogAnnotation(module="个人主页标签管理删除标签",operator="根据问题ID")
    public Result deleteUserTagById(@PathVariable("id") Long id){
        //int i = 10/0;  //测试统一异常拦截类效果
        return tagService.deleteUserTagById(id);
    }

    /**
     * 通过表单信息 新增标签
     * @param tag
     * @return
     */
    @PostMapping("addTag")
    @ApiOperation(value="新增标签",notes = "通过表单信息 ")
    @LogAnnotation(module="新增标签",operator="通过表单信息")
    public Result addTag(@RequestBody Tag tag){
        //int i = 10/0;  //测试统一异常拦截类效果
        return tagService.addTag(tag);
    }

    /**
     * 通过表单信息 编辑标签
     * @param tag
     * @return
     */
    @PostMapping("updateTag")
    @ApiOperation(value="编辑标签",notes = "通过表单信息")
    @LogAnnotation(module="编辑标签",operator="通过表单信息")
    public Result updateTag(@RequestBody Tag tag){
        //int i = 10/0;  //测试统一异常拦截类效果
        return tagService.updateTag(tag);
    }

    /**
     * --------------个人主页 收藏板块-------------
     */

    /**
     * 个人主页-收藏管理 收藏列表
     * @param likeParam
     * @return
     */
    @PostMapping("getUserLikeList")
    @ApiOperation(value="个人主页文章管理文章列表",notes = "根据表单信息查询")
    @LogAnnotation(module="个人主页文章管理文章列表",operator="根据表单信息查询")
    public Result getUserLikeList(@RequestBody LikeParam likeParam){
        //int i = 10/0;  //测试统一异常拦截类效果
        return likeService.getUserLikeList(likeParam);
    }

    /**
     * 个人主页-收藏管理-删除收藏
     * @param liId
     * @return
     */
    @PostMapping("deleteUserLikeById/{liId}")
    @ApiOperation(value="个人主页收藏管理删除收藏",notes = "根据收藏ID")
    @LogAnnotation(module="个人主页收藏管理删除收藏",operator="根据收藏ID")
    public Result deleteUserLikeById(@PathVariable("liId") Long liId){
        //int i = 10/0;  //测试统一异常拦截类效果
        return likeService.deleteUserLikeById(liId);
    }

    /**
     * --------------个人主页 关注板块-------------
     */

    /**
     * 个人主页-关注管理-关注列表
     * @param focusParam
     * @return
     */
    @PostMapping("getUserFocusList")
    @ApiOperation(value="个人主页关注管理关注列表",notes = "根据表单信息查询")
    @LogAnnotation(module="个人主页关注管理关注列表",operator="根据表单信息查询")
    public Result getUserFocusList(@RequestBody FocusParam focusParam){
        //int i = 10/0;  //测试统一异常拦截类效果
        return focusService.getUserFocusList(focusParam);
    }

    /**
     * 个人主页-关注管理-删除关注
     * @param fid
     * @return
     */
    @PostMapping("deleteUserFocusById/{fid}")
    @ApiOperation(value="个人主页关注管理删除关注",notes = "根据关注ID")
    @LogAnnotation(module="个人主页关注管理删除关注",operator="根据关注ID")
    public Result deleteUserFocusById(@PathVariable("fid") Long fid){
        //int i = 10/0;  //测试统一异常拦截类效果
        return focusService.deleteUserFocusById(fid);
    }

    /**
     * --------------个人主页 黑名单板块-------------
     */

    /**
     * 个人主页-黑名单管理-黑名单列表
     * @param blackListParam
     * @return
     */
    @PostMapping("getUserBlackList")
    @ApiOperation(value="个人主页黑名单管理黑名单列表",notes = "根据表单信息查询")
    @LogAnnotation(module="个人主页黑名单管理黑名单列表",operator="根据表单信息查询")
    public Result getUserBlackList(@RequestBody BlackListParam blackListParam){
        //int i = 10/0;  //测试统一异常拦截类效果
        return blackListService.getUserBlackList(blackListParam);
    }

    /**
     * 个人主页-黑名单管理-删除黑名单
     * @param bid
     * @return
     */
    @PostMapping("deleteUserBlackById/{bid}")
    @ApiOperation(value="个人主页黑名单管理删除黑名单",notes = "根据黑名单ID")
    @LogAnnotation(module="个人主页黑名单管理删除黑名单",operator="根据黑名单ID")
    public Result deleteUserBlackById(@PathVariable("bid") Long bid){
        //int i = 10/0;  //测试统一异常拦截类效果
        return blackListService.deleteUserBlackById(bid);
    }


    /**
     * --------------我的团队板块-------------
     */

    /**
     * 通过uid 查看用户是否有加入团队
     * @param uid
     * @return
     */
    @PostMapping("getUserTeamState/{uid}")
    @ApiOperation(value="查看用户是否有加入团队",notes = "通过uid")
    @LogAnnotation(module="查看用户是否有加入团队",operator="通过uid")
    public Result getUserTeamState(@PathVariable("uid") Long uid){

        return teamUserService.getUserTeamState(uid);
    }

    /**
     * 通过id 获取用户创建的团队信息
     * @param uid
     * @return
     */
    @GetMapping("getTeamByUid/{uid}")
    @ApiOperation(value="获取用户创建的团队信息",notes = "通过uid")
    @LogAnnotation(module="获取用户创建的团队信息",operator="通过uid")
    public Result getTeamByUid(@PathVariable("uid") Long uid){
        return teamService.getTeamByUid(uid);
    }

    /**
     * 通过表单信息 新增团队
     * @param teamParam
     * @return
     */
    @PostMapping("teamCreate")
    @ApiOperation(value="新增团队",notes = "通过表单信息 ")
    @LogAnnotation(module="新增团队",operator="通过表单信息")
    public Result teamCreate(@RequestBody TeamParam teamParam){

        return teamService.createTeam(teamParam);
    }

    /**
     * 通过表单信息 编辑团队
     * @param teamParam
     * @return
     */
    @PostMapping("teamEdit")
    @ApiOperation(value="编辑团队",notes = "通过表单信息 ")
    @LogAnnotation(module="编辑团队",operator="通过表单信息")
    public Result teamEdit(@RequestBody TeamParam teamParam){

        return teamService.updateTeam(teamParam);
    }

    /**
     * 根据用户ID、团队名称 查询我的团队列表
     * @param teamParam
     * @return
     */
    @PostMapping("getUserTeamList")
    @ApiOperation(value="查询我的团队列表",notes = "根据用户ID团队名称")
    @LogAnnotation(module="查询我的团队列表",operator="根据用户ID团队名称")
    public Result getUserTeamList(@RequestBody TeamParam teamParam){
        //System.out.println("UID"+teamParam.getUid());
        //System.out.println("团队名"+teamParam.getTName());
        return teamService.getUserTeamList(teamParam);
    }

    /**
     * 通过用户ID、团队ID 判断删除 退出团队
     * @param teamParam
     * @return
     */
    @PostMapping("deleteUserTeamById")
    @ApiOperation(value="判断删除退出团队",notes = "通过用户ID团队ID")
    @LogAnnotation(module="判断删除退出团队",operator="通过用户ID团队ID")
    public Result deleteUserTeamById(@RequestBody TeamParam teamParam){
        return teamService.deleteUserTeamById(teamParam);
    }

    /**
     *  --------------团队详情页面请求----------------
     */
    /**
     * 通过团队tid 获取团队信息
     * @param tid
     * @return
     */
    @GetMapping("getTeamByTid/{tid}")
    @ApiOperation(value="获取团队信息",notes = "通过tid")
    @LogAnnotation(module="获取团队信息",operator="通过tid")
    public Result getTeamByTid(@PathVariable("tid") Long tid){
        return teamService.getTeamByTid(tid);
    }

    /**
     * 团队管理-文章列表 tid
     * @param pageParams
     * @return
     */
    @PostMapping("getTeamArticleList")
    @ApiOperation(value="团队管理文章列表",notes = "根据表单信息查询")
    @LogAnnotation(module="团队管理文章列表",operator="根据表单信息查询")
    public Result getTeamArticleList(@RequestBody PageParams pageParams){
        //int i = 10/0;  //测试统一异常拦截类效果
        return articleService.getUserArticleList(pageParams);
    }

    /**
     * 团队管理 问题列表 tid
     * @param pageParams
     * @return
     */
    @PostMapping("getTeamProblemList")
    @ApiOperation(value="团队管理问题列表 ",notes = "根据表单信息查询")
    @LogAnnotation(module="团队管理问题列表",operator="根据表单信息查询")
    public Result getTeamProblemList(@RequestBody PageParams pageParams){
        //int i = 10/0;  //测试统一异常拦截类效果
        return problemService.getUserProblemList(pageParams);
    }

    /**
     * 通过tid、nickname 获取团队成员表
     * @param teamUserParam
     * @return
     */
    @PostMapping("getTeamUserList")
    @ApiOperation(value="获取团队成员列表",notes = "根据表单信息查询")
    @LogAnnotation(module="获取团队成员列表",operator="根据表单信息查询")
    public Result getTeamUserList(@RequestBody TeamUserParam teamUserParam){
        return teamUserService.getTeamUserList(teamUserParam);
    }

    /**
     * 通过成员表唯一ID 删除
     * @param tuId
     * @return
     */
    @PostMapping("deleteTeamUserBytuId/{tuId}")
    @ApiOperation(value="删除成员",notes = "根据成员表唯一ID")
    @LogAnnotation(module="删除成员",operator="根据成员表唯一ID")
    public Result deleteTeamUserBytuId(@PathVariable("tuId") Long tuId){
        //int i = 10/0;  //测试统一异常拦截类效果
        return teamUserService.deleteTeamUserBytuId(tuId);
    }

    /**
     * 通过表单信息 新增团队成员 根据uid 或 名称
     * @param teamUser
     * @return
     */
    @PostMapping("addTeamUser")
    @ApiOperation(value="新增团队成员",notes = "通过表单信息 ")
    @LogAnnotation(module="新增团队成员",operator="通过表单信息")
    public Result addTeamUser(@RequestBody TeamUser teamUser){

        return teamUserService.addTeamUser(teamUser);
    }









    /**
     * --------------作者主页板块-------------
     */
    // 通过文章id 获取作者用户信息
    @GetMapping("getUserByApId/{id}")
    @ApiOperation(value="获取作者用户信息",notes = "文章id")
    @LogAnnotation(module="获取作者用户信息",operator="文章id")
    public Result getUserByApId(@PathVariable("id") Long id){
        return sysUserService.getUserByApId(id);
    }

    /**
     * 通过发起id、被关注id  查询浏览者对文章作者的关注状态
     * @param focus
     * @return
     */
    @PostMapping("queryFocusState")
    @ApiOperation(value="查询浏览者的关注状态",notes = "通过发起id被关注id")
    @LogAnnotation(module="查询浏览者的关注状态",operator="通过发起id被关注id")
    public Result queryFocusState(@RequestBody Focus focus){
        //int i = 10/0;  //测试统一异常拦截类效果
        return sysUserService.queryFocusState(focus);
    }

    /**
     * 通过发起id、被关注id  添加关注信息
     * @param focus
     * @return
     */
    @PostMapping("addFocusUser")
    @ApiOperation(value="添加关注信息",notes = "通过发起id被关注id")
    @LogAnnotation(module="添加关注信息",operator="通过发起id被关注id")
    public Result addFocusUser(@RequestBody Focus focus){
        //int i = 10/0;  //测试统一异常拦截类效果
        return sysUserService.addFocusState(focus);
    }

    /**
     * 通过发起id、被关注id  删除关注信息
     * @param focus
     * @return
     */
    @PostMapping("deleteFocusUser")
    @ApiOperation(value="删除关注信息",notes = "通过发起id被关注id")
    @LogAnnotation(module="删除关注信息",operator="通过发起id被关注id")
    public Result deleteFocusUser(@RequestBody Focus focus){
        //int i = 10/0;  //测试统一异常拦截类效果
        return sysUserService.deleteFocusUser(focus);
    }

}
