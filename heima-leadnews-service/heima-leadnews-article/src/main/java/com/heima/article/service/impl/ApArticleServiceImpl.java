package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleConfigService;
import com.heima.article.service.ApArticleContentService;
import com.heima.article.service.ApArticleService;
import com.heima.feign.behavior.BehaviorFeignClient;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ArticleFeignDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.dtos.BehaviorResultDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.behavior.dtos.ArticleInfoDto;
import com.heima.model.common.constants.ArticleConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.Content;
import com.heima.model.wemedia.dtos.WmNewsAuthorDto;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.utils.common.ThreadLocalUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.HTML;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

/**
 * @ClassName ApArticleServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.18
 * @Description
 */

@Slf4j
@Service
@Transactional
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle>
        implements ApArticleService {

    private static final Integer DEFAULT_SIZE = 10;

    private static final short MAX_PAGE_SIZE = 50;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleService apArticleService;

    @Autowired
    private ApArticleContentService apArticleContentService;

    @Autowired
    private ApArticleConfigService apArticleConfigService;

    @Autowired
    private Configuration configuration;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private BehaviorFeignClient behaviorFeignClient;


    /**
     * 加载首页, 根据参数加载文章列表, 1为加载更多 2为加载最新
     * @Param [articleHomeDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult articleLoad(ArticleHomeDto articleHomeDto, Short loadtype) {
        // 校验参数, 获取传回的分页size
        Integer pageSize = articleHomeDto.getSize();
        // 判断传回的size是否为空或为0
        if (pageSize == null || pageSize == 0) {
            // 为空或为0, 设置size默认为10
            pageSize = DEFAULT_SIZE;
        }
        // 若传回的数据较大则使用最大默认值50
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        // 设置size
        articleHomeDto.setSize(pageSize);

        // 校验类型参数, 若loadtype不是加载更多, 也不是加载最新
        if (!loadtype.equals(ArticleConstants.LOADTYPE_LOAD_MORE) &&
                !loadtype.equals(ArticleConstants.LOADTYPE_LOAD_NEW)) {
            // 设置默认为加载更多
            loadtype = ArticleConstants.LOADTYPE_LOAD_MORE;
        }

        // 校验文章频道ID, 判断传回的tag是否为空或者空字符串
        if (StringUtils.isEmpty(articleHomeDto.getTag())) {
            // 设置
            articleHomeDto.setTag(ArticleConstants.DEFAULT_TAG);
        }

        // 时间校验, 判断传回的时间是否为空
        if (articleHomeDto.getMaxBehotTime() == null) {
            articleHomeDto.setMaxBehotTime(new Date());
        }
        // 时间校验, 判断传回的时间是否为空
        if (articleHomeDto.getMinBehotTime() == null) {
            articleHomeDto.setMinBehotTime(new Date());
        }

        // 查询数据
        List<ApArticle> apArticles = apArticleMapper.loadArticleList(articleHomeDto, loadtype);

        // 封装结果并返回
        return ResponseResult.okResult(apArticles);
    }

    /**
     * APP端文章保存
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult<Map<String, String>> articleFeignClient(ArticleFeignDto articleFeignDto) throws IOException, TemplateException {
        // 判断文章是否已经
        // 文章信息保存在ap_article表
        ApArticle apArticle = new ApArticle();
        apArticle.setTitle(articleFeignDto.getTitle());
        apArticle.setAuthorId(articleFeignDto.getUserId().longValue());
        apArticle.setAuthorName(articleFeignDto.getAuthorName());
        apArticle.setChannelId(articleFeignDto.getChannelId());
        apArticle.setChannelName(articleFeignDto.getChannelName());
        apArticle.setLayout(articleFeignDto.getType());
        apArticle.setFlag((byte) 0);
        apArticle.setImages(articleFeignDto.getImages());
        apArticle.setLabels(articleFeignDto.getLabels());
        apArticle.setLikes(0);
        apArticle.setCollection(0);
        apArticle.setComment(0);
        apArticle.setViews(0);
        apArticle.setCreatedTime(new Date());
        // 判断设置的发布时间是否在当前时间之后, 大于当前时间
        if (articleFeignDto.getPublishTime().after(new Date())) {
            // 设置当前时间
            apArticle.setPublishTime(new Date());
        } else {
            // 小于于当前时间
            apArticle.setPublishTime(articleFeignDto.getPublishTime());
        }

        // TODO 生成文章详情静态化页面, 保存在minio中
        String staticUrl = getStaticUrl(articleFeignDto);
        // 设置html的url
        apArticle.setStaticUrl(staticUrl);

        apArticleService.save(apArticle);

        // 获取apArticle_id
        Long articleId = apArticle.getId();
        // 文章信息保存保存在ap_article_content表
        ApArticleContent apArticleContent = new ApArticleContent();
        apArticleContent.setArticleId(articleId);
        apArticleContent.setContent(articleFeignDto.getContent());
        apArticleContentService.save(apArticleContent);

        // 保存在ap_article_config表
        ApArticleConfig apArticleConfig = new ApArticleConfig();
        apArticleConfig.setArticleId(articleId);
        apArticleConfig.setIsComment(true);
        apArticleConfig.setIsForward(true);
        apArticleConfig.setIsDown(false);
        apArticleConfig.setIsDelete(false);
        apArticleConfigService.save(apArticleConfig);

        Map<String, String> data = new HashMap<>();
        data.put("articleId", String.valueOf(articleId));
        data.put("staticUrl", staticUrl);
        // 返回apArticle_id
        return ResponseResult.okResult(data);
    }


    private String getStaticUrl(ArticleFeignDto articleFeignDto) throws IOException, TemplateException {
        // 获取content内容
        String content = articleFeignDto.getContent();
        // 存放html文件url
        StringBuilder htmlFileUrl = new StringBuilder();
        // 判断content不为空并且不为空字符串
        if (content != null && StringUtils.isNoneBlank(content)) {
            // 文章内容通过freemarker生成html文件
            Template template = configuration.getTemplate("article.ftl");
            // 将content反序列化为数组
            List<Content> contents = JSON.parseArray(content, Content.class);
            // 创建map存放content
            Map<String, Object> params = new HashMap<>();
            // 将内容存在map中
            params.put("content", contents);
            // 创建在字符串缓冲区中收集输出的字符流
            StringWriter stringWriter = new StringWriter();
            // process()模板方法, 需要传入map集合和字符字符输出流
            template.process(params, stringWriter);
            // 生成html文件名
            String fileName = UUID.randomUUID().toString() + ".html";
            // 获取字节数组
            byte[] bytesInptStream = stringWriter.toString().getBytes();
            // 构建字节输入流, ByteArrayInputStream将内存中的缓冲区作为InputStream, 输入源是一个字节数组
            InputStream inputStream = new ByteArrayInputStream(bytesInptStream);
            // 将html文件上传到minio中
            String fileUrl = fileStorageService.uploadHtmlFile("", fileName, inputStream);
            // 存放到StringBuilder
            htmlFileUrl.append(fileUrl);
        }
        // 返回url
        return htmlFileUrl.toString();
    }

    /**
     * 获取作者名, 封装WmNewsAuthorDto
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult articleAuthorFeignClient(Map<String, List<WmNews>> wmNewsMap) {
        // 获取WmNews集合
        List<WmNews> wmNews = wmNewsMap.get("wmNews");
        // 存放WmNewsAuthorDto
        List<WmNewsAuthorDto> wmNewsAuthorDtoList = new ArrayList<>();
        // 遍历
        for (WmNews wmNew : wmNews) {
            WmNewsAuthorDto wmNewsAuthorDto = new WmNewsAuthorDto();
            // 拷贝数据
            BeanUtils.copyProperties(wmNew, wmNewsAuthorDto);
            // 获取article_id
            Long articleId = wmNew.getArticleId();
            // 根据id查询article
            ApArticle apArticle = apArticleService.getById(articleId);
            // 判断查询到的是否为空
            if (apArticle != null) {
                // 设置值
                wmNewsAuthorDto.setAuthorName(apArticle.getAuthorName());
            }
            // 存入集合
            wmNewsAuthorDtoList.add(wmNewsAuthorDto);
        }
        return ResponseResult.okResult(wmNewsAuthorDtoList);
    }

    /**
     * 回显用户行为
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult loadUserBehavior(ArticleInfoDto articleInfoDto) {
        // 获取用户id
        Integer userId = ThreadLocalUtils.getUserId();
        BehaviorResultDto behaviorResultDto = new BehaviorResultDto();
        // 判断是用户登录
        if (userId != 0) {
            // 调用feign, 查询登录用户对文章是否进行了点赞
            ResponseResult<Boolean> responseResult = behaviorFeignClient
                    .loadUserBehavior(articleInfoDto.getArticleId(), userId);
            // 获取返回结果
            Boolean isLike = responseResult.getData();
            // 判断结果是否为true
            if (isLike) {
                behaviorResultDto.setIslike(true);
            }
            // TODO 开发完成
            // 查询登录用户对文章是否进行了收藏
            // 查询登录用户对文章的作者否进行了关注
            // 查询登录用户对文章的作者否进行了不喜欢
        }
        // 返回结果
        return ResponseResult.okResult(behaviorResultDto);
    }

    /**
     * 计算热点文章
     * @Param [date]
     * @Return {@link ResponseResult<List<ApArticle>>}
     */
    @Override
    public ResponseResult<List<ApArticle>> findArticleByPublishTime(Date date) {
        // 查询
        List<ApArticle> apArticles = apArticleMapper.findArticleByPublishTime(date);
        return ResponseResult.okResult(apArticles);
    }

}
