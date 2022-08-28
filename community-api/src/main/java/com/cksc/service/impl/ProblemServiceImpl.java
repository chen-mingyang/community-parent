package com.cksc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.dao.dos.Archives;
import com.cksc.dao.mapper.*;
import com.cksc.dao.pojo.*;
import com.cksc.service.*;
import com.cksc.utils.UserThreadLocal;
import com.cksc.vo.*;
import com.cksc.vo.params.ArticleParam;
import com.cksc.vo.params.PageParams;
import com.cksc.vo.params.ProblemParam;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/14 11:55
 * @description 问题服务实现类
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

    @Override
    public Result listProblems(PageParams pageParams) {
        //Mybatis-plus分页
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        //apType=2 问题
        IPage<Article> articleIPage = problemMapper.listProblems(
                page,
                2,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords(); //在分页中得到List<Article>

        ////文章浏览量计数加入redis缓存
        //for (Article record : records) {
        //    String viewCount = (String) redisTemplate.opsForHash().get("view_count", String.valueOf(record.getId()));
        //    if (viewCount != null){
        //        record.setViewCounts(Integer.parseInt(viewCount));
        //    }
        //}

        //返回VO
        return Result.success(copyList(records,true,true));
    }

    ///**
    // * 1. 分页查询 article数据库表
    // * 没有文章归档查询文章列表 使用
    // */
    //@Override
    //public Result listArticle(PageParams pageParams) {
    //    //myabatis-plus分页工具
    //    Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
    //    //LambdaQueryWrapper是mybatis plus中的一个条件构造器对象，用于函数单表查询
    //    LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
    //
    //    //在文章分类展示区，点击分类，展示在此分类下的文章列表
    //    if (pageParams.getCategoryId() != null){
    //        // and category_id=#{categoryId}
    //        queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
    //    }
    //    //定义一个存文章ID列表
    //    List<Long> articleIdList = new ArrayList<>();
    //    //在标签展示区，点击标签，展示在此标签下的文章列表
    //    if (pageParams.getTagId() != null){
    //        //加入标签 条件查询
    //        //article表中 并没有tag字段 一篇文章 有多个标签
    //        //article_tag  article_id 1 : n tag_id
    //
    //        //1 根据标签ID，去查询文章-标签关系表，得到文章-标签关系列表
    //        //2 用循环把文章ID逐个加入列表中
    //        //3 把这个文章ID列表加入查询文章条件
    //        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
    //        articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
    //        List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
    //        for (ArticleTag articleTag : articleTags) {
    //            articleIdList.add(articleTag.getArticleId());
    //        }
    //        //列表查询条件
    //        if (articleIdList.size() > 0){
    //            // and id in(1,2,3)
    //            queryWrapper.in(Article::getId,articleIdList);
    //        }
    //    }
    //
    //    //是否置顶进行排序
    //    //order by create_date desc 排序
    //    queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
    //    //得到文章列表分页
    //    Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
    //    //取出分页后的文章列表信息
    //    List<Article> records = articlePage.getRecords();
    //    //能直接返回吗？ 很明显不能 定义返回ArticleVo对象
    //    List<ArticleVo> articleVoList = copyList(records,true,true);
    //    return Result.success(articleVoList);
    //}

    @Override
    public Result hotProblems(int limit) {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //apType=2 问题
        queryWrapper.eq(Article::getApType,2);
        queryWrapper.eq(Article::getApState,1);
        //根据浏览量排序-降序
        queryWrapper.orderByDesc(Article::getViewCounts);
        //只取文章的ID和Title
        queryWrapper.select(Article::getId,Article::getTitle);
        //限制取前几个
        queryWrapper.last("limit "+limit);

        //select id,title from article order by view_counts desc limit 5
        //传入查询条件 得到文章列表
        List<Article> articles = problemMapper.selectList(queryWrapper);
        //转换成VO对象返回
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newProblems(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //apType=2 问题
        queryWrapper.eq(Article::getApType,2);
        queryWrapper.eq(Article::getApState,1);
        //根据时间排序-降序
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        //select id,title from article order by create_date desc desc limit 5
        List<Article> articles = problemMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result archiveProblems() {
        List<Archives> archivesList = problemMapper.archiveProblems();
        return Result.success(archivesList);
    }


    @Autowired
    private ThreadService threadService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public Result findProblemById(Long problemId) {
        /**
         * 1. 根据id查询 文章信息
         * 2. 根据bodyId和categoryid 去做关联查询
         */
        Article article = this.problemMapper.selectById(problemId);
        ArticleVo articleVo = copy(article, true, true,true,true);

        //查看完文章了，新增阅读数，有没有问题呢？
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁，阻塞其他的读操作，性能就会比较低
        // 更新 增加了此次接口的 耗时 如果一旦更新出问题，异步更新 不能影响 查看文章的操作

        //线程池  可以把更新操作 扔到线程池中去执行，和主线程就不相关了
        threadService.updateArticleViewCount2(problemMapper,article);

        //String viewCount = (String) redisTemplate.opsForHash().get("view_count", String.valueOf(articleId));
        //if (viewCount != null){
        //    articleVo.setViewCounts(Integer.parseInt(viewCount));
        //}

        return Result.success(articleVo);
    }

    @Override
    public Result publish(ProblemParam problemParam) {
        //此接口路径 要加入到登录拦截WebMVCConfig当中 UserThreadLocal.get()才能获取值
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser.getId());
        /**
         * 1. 发布文章 目的 构建Article对象
         * 2. 作者id  当前的登录用户
         * 3. 标签  要将标签加入到 关联列表当中
         * 4. body 内容存储 article bodyId
         */
        Article article = new Article();
        boolean isEdit = false; //默认不是编辑，是发布
        if (problemParam.getId() != null){
            //编辑
            article = new Article();
            article.setId(problemParam.getId());
            article.setTid(problemParam.getTid());
            article.setTitle(problemParam.getTitle());
            article.setSummary(problemParam.getSummary());
            article.setApType(2);
            article.setApState(1);
            article.setCategoryId(Long.parseLong(problemParam.getCategory().getId()));
            problemMapper.updateById(article);
            isEdit = true; //传过来的文章ID不为空，则改为true
        }else{
            //发布
            article = new Article();
            article.setAuthorId(sysUser.getId());
            article.setTid(problemParam.getTid());
            article.setWeight(Article.Article_Common);
            article.setViewCounts(0);
            article.setTitle(problemParam.getTitle());
            article.setSummary(problemParam.getSummary());
            article.setCommentCounts(0);
            article.setUpCounts(0);
            article.setLikeCounts(0);
            article.setCreateDate(System.currentTimeMillis()); //当前时间
            article.setCategoryId(Long.parseLong(problemParam.getCategory().getId()));
            article.setApType(2);
            article.setApState(1);
            //插入之后 会生成一个文章id
            this.problemMapper.insert(article);
        }

        //tag 标签存储
        List<TagVo> tags = problemParam.getTags();
        if (tags != null){
            Long articleId = article.getId();
            if (isEdit){
                //编辑下 先删除
                LambdaQueryWrapper<ArticleTag> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(ArticleTag::getArticleId,articleId);
                articleTagMapper.delete(queryWrapper);
            }
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId())); //强制转换
                articleTag.setArticleId(articleId);
                //插入问题标签
                articleTagMapper.insert(articleTag);
            }
        }

        //body 内容存储
        if (isEdit){
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(problemParam.getBody().getContent());
            articleBody.setContentHtml(problemParam.getBody().getContentHtml());

            LambdaUpdateWrapper<ArticleBody> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(ArticleBody::getArticleId,article.getId());
            problemBodyMapper.update(articleBody, updateWrapper);
        }else {
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(problemParam.getBody().getContent());
            articleBody.setContentHtml(problemParam.getBody().getContentHtml());
            //插入文章内容
            problemBodyMapper.insert(articleBody);
            article.setBodyId(articleBody.getId());
            //更新文章表中内容ID
            problemMapper.updateById(article);
        }
        //返回文章ID 返回ArticleVO有精度损失，要加序列化
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());

        //编辑，使用消息队列，更新缓存
        if (isEdit){
            //发送一条消息给rocketmq 当前文章更新了，更新一下缓存吧
            ArticleMessage articleMessage = new ArticleMessage();
            articleMessage.setArticleId(article.getId());
            rocketMQTemplate.convertAndSend("blog-update-problem",articleMessage);
        }

        return Result.success(map);
    }

    @Override
    public Result getUserProblemList(PageParams pageParams) {

        UserArticleVo userArticleVo = new UserArticleVo();
        //Mybatis-plus分页
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        //apType=1 文章
        IPage<Article> articleIPage = problemMapper.getUserProblemList(
                page,
                2,
                pageParams.getUid(),
                pageParams.getTid(),
                pageParams.getTitle());
        List<Article> records = articleIPage.getRecords(); //在分页中得到List<Article>


        userArticleVo.setTotal((int) articleIPage.getTotal());

        List<ArticleVo> articleVoList = copyList(records, false, true
        );
        userArticleVo.setArticleVoList(articleVoList);
        //返回VO
        return Result.success(userArticleVo);
    }

    @Override
    public Result deleteUserProblemById(Long id) {

        //根据文章ID 目前没进行数据库表级联操作 所以逐个删除
        //1 删除问题-标签表
        HashMap<String,Object> map = new HashMap<>();
        map.put("article_id",id);
        articleTagMapper.deleteByMap(map);
        //2 删除问题内容表
        problemBodyMapper.deleteByMap(map);
        //3 删除问题评论 点赞 收藏
        commentMapper.deleteByMap(map);
        HashMap<String,Object> map2 = new HashMap<>();
        map.put("ap_id",id);
        thumbsUpMapper.deleteByMap(map2);
        likeMapper.deleteByMap(map2);

        //4 删除问题表
        problemMapper.deleteById(id);
        return Result.success("问题删除成功");
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
        ArticleBody articleBody = problemBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
