package com.cksc.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.dao.mapper.*;
import com.cksc.dao.pojo.Article;
import com.cksc.dao.pojo.ArticleBody;
import com.cksc.dao.pojo.SysUser;
import com.cksc.dao.pojo.Team;
import com.cksc.service.*;
import com.cksc.vo.*;
import com.cksc.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/20 17:25
 * @description 搜索服务实现类
 */

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private TeamService teamService;

    @Override
    public Result getSearchIndexList(PageParams pageParams) {
        UserArticleVo userArticleVo = new UserArticleVo();
        //Mybatis-plus分页
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        //apType=1 文章
        IPage<Article> articleIPage = articleMapper.getSearchIndexList(
                page,
                pageParams.getTitle());
        List<Article> records = articleIPage.getRecords(); //在分页中得到List<Article>


        userArticleVo.setTotal((int) articleIPage.getTotal());

        List<ArticleVo> articleVoList = copyList(records, false, true);
        userArticleVo.setArticleVoList(articleVoList);
        //返回VO
        return Result.success(userArticleVo);
    }


    //List<Article> 转移到 List<ArticleVo> 首页文章列表用
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
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

    @Autowired
    private CategoryService categoryService;

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
                Team team = teamService.findTeamById(article.getTid());
                TeamVo teamVo = new TeamVo();
                //对象复制
                BeanUtils.copyProperties(team,teamVo);
                teamVo.setTid(String.valueOf(team.getTid()));
                articleVo.setTeam(teamVo);
            }

            ////这里返回UserVo 里面ID为String类型，所以不丢失精度
            //articleVo.setSysUser(sysUser);
            UserVo userVo = new UserVo();
            //对象复制
            BeanUtils.copyProperties(sysUser,userVo);
            userVo.setId(String.valueOf(sysUser.getId()));
            articleVo.setSysUser(userVo);
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
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

}
