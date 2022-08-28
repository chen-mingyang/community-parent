package com.cksc.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.admin.dao.mapper.*;
import com.cksc.admin.dao.pojo.Article;
import com.cksc.admin.dao.pojo.ArticleBody;
import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.dao.pojo.Team;
import com.cksc.admin.service.*;
import com.cksc.admin.vo.ArticleBodyVo;
import com.cksc.admin.vo.ArticleVo;
import com.cksc.admin.vo.PageResult;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.PageParams;
import com.cksc.admin.vo.params.ProblemParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/23 17:09
 * @description 文章服务实现类
 */

@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private ProblemMapper problemMapper;

    //没有外键级联，导致逐个删除
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private ProblemBodyMapper problemBodyMapper;
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private ThumbsUpMapper thumbsUpMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public Result getProblemList(PageParams pageParams) {
        //分页 列表泛型、total
        PageResult pageResult = new PageResult();
        //Mybatis-plus分页
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        //apType=1 文章
        IPage<Article> articleIPage = problemMapper.getProblemList(
                page,
                2,
                pageParams.getTitle());
        List<Article> records = articleIPage.getRecords(); //在分页中得到List<Article>


        pageResult.setTotal((int) articleIPage.getTotal());

        List<ArticleVo> articleVoList = copyList(records, true, true,true);
        pageResult.setList(articleVoList);
        //返回VO
        return Result.success(pageResult);
    }

    @Override
    public Result deleteProblemById(Long id) {
        //目前没进行数据库表级联操作 所以逐个删除
        //1 删除文章-标签表
        HashMap<String,Object> map = new HashMap<>();
        map.put("article_id",id);
        articleTagMapper.deleteByMap(map);
        //2 删除文章内容表
        problemBodyMapper.deleteByMap(map);
        //3 删除文章评论 文章点赞 文章收藏
        commentMapper.deleteByMap(map);
        HashMap<String,Object> map2 = new HashMap<>();
        map.put("ap_id",id);
        thumbsUpMapper.deleteByMap(map2);
        likeMapper.deleteByMap(map2);
        //4 删除文章表
        problemMapper.deleteById(id);

        return Result.success("问题删除成功");
    }

    @Override
    public Result changeProblemState(ProblemParam problemParam) {
        Article article = new Article();
        article.setId(problemParam.getId());
        article.setApState(problemParam.getApState());
        article.setUpdateTime(new Date());
        problemMapper.updateById(article);
        return Result.success("更新问题状态成功");
    }

    //List<Article> 转移到 List<ArticleVo> 首页文章列表用
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,isCategory));
        }
        return articleVoList;
    }

    //List<Article> 转移到 List<ArticleVo> 查看文章详情用
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    //Article对象 转移到 ArticleVo对象
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId())); //防止精度丢失使用string类型
        //对象复制
        BeanUtils.copyProperties(article,articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口 都需要标签 ，作者信息
        if (isTag){
            // 获取文章ID
            Long articleId = article.getId();
            //根据文章ID查询对应的标签列表
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            // 获取文章ID
            Long authorId = article.getAuthorId();
            // 根据文章ID查询用户
            SysUser sysUser = sysUserService.findUserById(authorId);
            //加入团队信息
            if(article.getTid()!=null){
                //根据团队ID 获取团队名称
                Team team = teamService.findTeamByTid(article.getTid());
                articleVo.setTeam(team);
            }

            articleVo.setSysUser(sysUser);

        }
        if (isBody){
            Long bodyId = article.getBodyId();
            //根据内容ID查询文章内容
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            //根据分类ID查询文章分类信息
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    //根据内容ID查询文章内容
    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = problemBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
