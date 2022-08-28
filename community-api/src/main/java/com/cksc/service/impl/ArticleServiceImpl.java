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
 * @date 2022/3/7 0:58
 * @description 文章实现类
 */

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    //没有外键级联，导致逐个删除
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
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
    public Result listArticles(PageParams pageParams) {
        //Mybatis-plus分页
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        //apType=1 文章
        IPage<Article> articleIPage = articleMapper.listArticles(
                page,
                1,
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

        //返回文章 VO
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
    public Result hotArticle(int limit) {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //apType=1 文章
        queryWrapper.eq(Article::getApType,1);
        queryWrapper.eq(Article::getApState,1);
        //根据浏览量排序-降序
        queryWrapper.orderByDesc(Article::getViewCounts);
        //只取文章的ID和Title
        queryWrapper.select(Article::getId,Article::getTitle);
        //限制取前几个
        queryWrapper.last("limit "+limit);

        //select id,title from article order by view_counts desc limit 5
        //传入查询条件 得到文章列表
        List<Article> articles = articleMapper.selectList(queryWrapper);
        //转换成VO对象返回
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //apType=1 文章
        queryWrapper.eq(Article::getApType,1);
        queryWrapper.eq(Article::getApState,1);
        //根据时间排序-降序
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        //select id,title from article order by create_date desc desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }


    @Autowired
    private ThreadService threadService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1. 根据id查询 文章信息
         * 2. 根据bodyId和categoryid 去做关联查询
         */
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true,true,true);

        //查看完文章了，新增阅读数，有没有问题呢？
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁，阻塞其他的读操作，性能就会比较低
        // 更新 增加了此次接口的 耗时 如果一旦更新出问题，异步更新 不能影响 查看文章的操作

        //线程池  可以把更新操作 扔到线程池中去执行，和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper,article);

        //String viewCount = (String) redisTemplate.opsForHash().get("view_count", String.valueOf(articleId));
        //if (viewCount != null){
        //    articleVo.setViewCounts(Integer.parseInt(viewCount));
        //}

        return Result.success(articleVo);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
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
        if (articleParam.getId() != null){
            //编辑
            article = new Article();
            article.setId(articleParam.getId());
            article.setTid(articleParam.getTid());
            article.setTitle(articleParam.getTitle());
            article.setSummary(articleParam.getSummary());
            article.setApType(1);
            article.setApState(1);
            article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
            articleMapper.updateById(article);
            isEdit = true; //传过来的文章ID不为空，则改为true
        }else{
            //发布
            article = new Article();
            article.setAuthorId(sysUser.getId());
            article.setTid(articleParam.getTid());
            article.setWeight(Article.Article_Common);
            article.setViewCounts(0);
            article.setTitle(articleParam.getTitle());
            article.setSummary(articleParam.getSummary());
            article.setCommentCounts(0);
            article.setUpCounts(0);
            article.setLikeCounts(0);
            article.setCreateDate(System.currentTimeMillis()); //当前时间
            article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
            article.setApType(1);
            article.setApState(1);
            //插入之后 会生成一个文章id article.getId()
            this.articleMapper.insert(article);
        }

        //tag 标签存储
        List<TagVo> tags = articleParam.getTags();
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
                //插入文章标签
                articleTagMapper.insert(articleTag);
            }
        }

        //body 内容存储
        if (isEdit){
            //编辑
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(articleParam.getBody().getContent());
            articleBody.setContentHtml(articleParam.getBody().getContentHtml());

            LambdaUpdateWrapper<ArticleBody> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(ArticleBody::getArticleId,article.getId());
            articleBodyMapper.update(articleBody, updateWrapper);
        }else {
            //发布
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(articleParam.getBody().getContent());
            articleBody.setContentHtml(articleParam.getBody().getContentHtml());
            //插入文章内容
            articleBodyMapper.insert(articleBody);
            article.setBodyId(articleBody.getId());
            //更新文章表中内容ID
            articleMapper.updateById(article);
        }
        //返回文章ID 返回ArticleVO有精度损失，要加序列化
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());

        //编辑，使用消息队列，更新缓存
        if (isEdit){
            //发送一条消息给rocketmq 当前文章更新了，更新一下缓存吧
            ArticleMessage articleMessage = new ArticleMessage();
            articleMessage.setArticleId(article.getId());
            //发送消息到消息队列，给定消息名字、消息内容
            rocketMQTemplate.convertAndSend("blog-update-article",articleMessage);
        }

        return Result.success(map);
    }

    @Override
    public Result getUserArticleList(PageParams pageParams) {

        UserArticleVo userArticleVo = new UserArticleVo();
        //Mybatis-plus分页
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        //apType=1 文章
        IPage<Article> articleIPage = articleMapper.getUserArticleList(
                page,
                1,
                pageParams.getUid(),
                pageParams.getTid(),
                pageParams.getTitle());
        List<Article> records = articleIPage.getRecords(); //在分页中得到List<Article>


        userArticleVo.setTotal((int) articleIPage.getTotal());

        List<ArticleVo> articleVoList = copyList(records, false, true);
        userArticleVo.setArticleVoList(articleVoList);
        //返回VO
        return Result.success(userArticleVo);
    }

    @Override
    public Result deleteUserArticleById(Long id) {
        //目前没进行数据库表级联操作 所以逐个删除
        //1 删除文章-标签表
        HashMap<String,Object> map = new HashMap<>();
        map.put("article_id",id);
        articleTagMapper.deleteByMap(map);
        //2 删除文章内容表
        articleBodyMapper.deleteByMap(map);
        //3 删除文章评论 文章点赞 文章收藏
        commentMapper.deleteByMap(map);
        HashMap<String,Object> map2 = new HashMap<>();
        map.put("ap_id",id);
        thumbsUpMapper.deleteByMap(map2);
        likeMapper.deleteByMap(map2);
        //4 删除文章表
        articleMapper.deleteById(id);

        return Result.success("文章删除成功");
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

            ////这里返回UserVo 里面ID为String类型，所以不丢失精度
            //articleVo.setSysUser(sysUser);
            UserVo userVo = new UserVo();
            //对象复制
            BeanUtils.copyProperties(sysUser,userVo);
            userVo.setId(String.valueOf(sysUser.getId()));
            articleVo.setSysUser(userVo);

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
