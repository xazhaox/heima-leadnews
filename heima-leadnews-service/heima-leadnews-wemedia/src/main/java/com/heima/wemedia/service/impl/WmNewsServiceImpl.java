package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.feign.article.ArticleFeignClient;
import com.heima.model.article.dtos.ArticleFeignDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.*;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.common.ThreadLocalUtils;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName WmNewsService.java
 * @Author xazhao
 * @Create 2022.08.20
 * @Description
 */

@Slf4j
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews>
        implements WmNewsService {

    @Autowired
    private WmMaterialService wmMaterialService;

    @Autowired
    private WmNewsMaterialService wmNewsMaterialService;

    @Autowired
    private WmNewsService wmNewsService;

    @Autowired
    private ArticleContentReviewService articleContentReviewService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ArticleFeignClient articleFeignClient;

    @Autowired
    private WmUserService wmUserService;

    /**
     * 自媒体文章分页查询
     * @Param [wmNewsPageReqDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult pageList(WmNewsPageReqDto wmNewsPageReqDto) {
        // 判断传入的数据是否合法
        wmNewsPageReqDto.checkParam();
        // 分页查询器
        IPage<WmNews> pageInfo = new Page<>(wmNewsPageReqDto.getPage(), wmNewsPageReqDto.getSize());
        // 条件查询
        LambdaUpdateWrapper<WmNews> queryWrapper = new LambdaUpdateWrapper<>();

        // 定位当前登录的用户
        queryWrapper.eq(WmNews::getUserId, ThreadLocalUtils.getUserId());

        // 判断是否传回状态
        if (wmNewsPageReqDto.getStatus() != null) {
            // 添加根据状态查询的条件
            queryWrapper.eq(WmNews::getStatus, wmNewsPageReqDto.getStatus());
        }

        // 判断是否传回所属频道ID
        if (wmNewsPageReqDto.getChannelId() != null) {
            // 添加根据属频道ID查询的条件
            queryWrapper.eq(WmNews::getChannelId, wmNewsPageReqDto.getChannelId());
        }

        // 判断是否传回关键字
        if (!StringUtils.isBlank(wmNewsPageReqDto.getKeyword())) {
            // 添加根据关键字模糊查询条件
            queryWrapper.like(WmNews::getTitle, wmNewsPageReqDto.getKeyword());
        }

        // 判断是否传回时间
        if (wmNewsPageReqDto.getBeginPubDate() != null &&
                wmNewsPageReqDto.getEndPubDate() != null) {
            // 添加根据时间查询的条件
            queryWrapper.between(WmNews::getPublishTime,
                    wmNewsPageReqDto.getBeginPubDate(),
                    wmNewsPageReqDto.getEndPubDate());
        }

        // 降序排序
        queryWrapper.orderByDesc(WmNews::getCreatedTime);

        // 分页查询
        IPage<WmNews> newsIPage = this.page(pageInfo, queryWrapper);
        // 获取数据
        List<WmNews> records = newsIPage.getRecords();

        // 分装返回结果
        PageResponseResult pageResponseResult = new PageResponseResult();
        pageResponseResult.setCurrentPage((int) pageInfo.getCurrent());
        pageResponseResult.setSize((int) pageInfo.getSize());
        pageResponseResult.setTotal((int) pageInfo.getTotal());
        pageResponseResult.setData(records);
        return pageResponseResult;
    }

    /**
     * 发布文章
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult postArticle(WmNewsDto wmNewsDto) throws Exception {
        // 判断传回的内容是否为空
        if (wmNewsDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,
                    "请补充完整数据");
        }
        WmNews wmNews = new WmNews();
        // 拷贝数据
        BeanUtils.copyProperties(wmNewsDto, wmNews);
        // 填充字段
        wmNews.setUserId(ThreadLocalUtils.getUserId());
        wmNews.setCreatedTime(new Date());
        if(wmNewsDto.getStatus() == WmNews.Status.SUBMIT.getCode()) {
            wmNews.setSubmitedTime(new Date());
        }
        // 封面类型是自动, 根据文章内容中的图片选择封面, 获取文章内容
        String content = wmNews.getContent();
        // 调用方法, 解析数据, 存放内容中的素材
        List<String> contentList = parseArticleData(content);
        // 存放封面素材
        List<String> coverList = new ArrayList<>();
        // 判断用户提交的封面类型是否是 -1自动, 1单图, 3三图, 0无图
        if (wmNews.getType() != -1) {
            // 用户选择的不是自动
            coverList = wmNewsDto.getImages();
            // 将集合中的list转为string, 以逗号分割
            String contenString = StringUtils.join(coverList, ",");
            wmNews.setImages(contenString);
        } else { // 用户的选择是自动
            coverList = new ArrayList<>();
            // 初始类型
            wmNews.setType((short) 0);
            // 判断内容中的素材不为空
            if (!contentList.isEmpty()) {
                // 判断文章中的素材是否小于三张
                if (contentList.size() < 3) {
                    // 取第一张照片作为封面
                    coverList = contentList.stream().limit(1).collect(Collectors.toList());
                    wmNews.setType((short) 1);
                } else {
                    // 文章中的素材大于等于三张
                    coverList = contentList.stream().limit(3).collect(Collectors.toList());
                    wmNews.setType((short) 3);
                }
            }
            // 将集合中的list转为string, 以逗号分割
            String contenImageString = StringUtils.join(coverList, ",");
            // 设置素材
            wmNews.setImages(contenImageString);
        }

        // 判断前端是否传回id, 传回说明文章已经存在 修改, 没传回id说明是第一次做新增
        if (wmNews.getId() == null) {
            // id不存在, 新增, 若用户点击提交审核将数据存入wm_news表
            this.save(wmNews);
        } else {
            // id存在, 修改
            this.updateById(wmNews);
        }

        // 若用户点击提交审核将文章关联的内容和素材及文章封面关联的素材关系存入wm_news_material表
        if (wmNewsDto.getStatus() == WmNews.Status.SUBMIT.getCode()) {
            // 获取wmNewsId
            Integer wmNewsId = wmNews.getId();

            // 构建修改条件
            LambdaUpdateWrapper<WmNewsMaterial> wrapper = new LambdaUpdateWrapper<>();
            // 根据wmNewsId构建条件
            wrapper.eq(WmNewsMaterial::getNewsId, wmNewsId);
            // 删除关联信息
            wmNewsMaterialService.remove(wrapper);

            // 保存文章内容和素材的关系
            for (String url : contentList) {
                LambdaUpdateWrapper<WmMaterial> queryWrapper = new LambdaUpdateWrapper<>();
                // 查询条件
                queryWrapper.eq(WmMaterial::getUrl, url);
                // 根据url查询数据
                WmMaterial wmMaterial = wmMaterialService.getOne(queryWrapper);
                WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
                wmNewsMaterial.setMaterialId(wmMaterial.getId());
                wmNewsMaterial.setNewsId(wmNewsId);
                wmNewsMaterial.setType((short) 0);
                wmNewsMaterial.setOrd((short) 1);
                wmNewsMaterialService.save(wmNewsMaterial);
            }
            // 保存文章封面和素材的关系
            for (String url : coverList) {
                LambdaUpdateWrapper<WmMaterial> queryWrapper = new LambdaUpdateWrapper<>();
                // 查询条件
                queryWrapper.eq(WmMaterial::getUrl, url);
                // 根据url查询数据
                WmMaterial wmMaterial = wmMaterialService.getOne(queryWrapper);
                WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
                wmNewsMaterial.setMaterialId(wmMaterial.getId());
                wmNewsMaterial.setNewsId(wmNewsId);
                wmNewsMaterial.setType((short) 1);
                wmNewsMaterial.setOrd((short) 1);
                wmNewsMaterialService.save(wmNewsMaterial);
            }
        }

        // TODO 文章内容审核
        articleContentReviewService.articleContentReview(wmNews);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 根据文章id查询文章
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult selectById(Integer id) {
        // 根据id查询
        WmNews wmNews = wmNewsService.getById(id);
        log.info("wmNews : {}", wmNews);
        // 判断是否为空
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }
        // 封装回显的数据
        ResponseResult responseResult = ResponseResult.okResult(wmNews);
        // 返回数据
        return responseResult;
    }

    /**
     * 删除文章
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult deleteArticle(Integer id) {
        // 判断传回的id是否为空
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 查询wm_news表
        WmNews wmNews = wmNewsService.getById(id);
        log.info("wmNews : {}", wmNews);
        // 判断文章是否已经发布, status为9表示以发布
        if (wmNews.getStatus() == WmNews.Status.PUBLISHED.getCode()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,
                    "该文章已被发布, 不可删除");
        }
        //
        // 删除文章
        wmNewsService.removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 文章上下架
     * @Param [wmNewsEnableDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult removeArticle(WmNewsEnableDto wmNewsEnableDto) {
        // 查询
        WmNews wmNews = wmNewsService.getById(wmNewsEnableDto.getId());
        // 判断是否查到文章
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 判断查询出的文章状态是否为9
        if (wmNews.getStatus() != WmNews.Status.PUBLISHED.getCode()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 文章状态为9, 将enable字段值修改为传回的值
        wmNews.setEnable(wmNewsEnableDto.getEnable());
        // 修改数据库
        wmNewsService.updateById(wmNews);
        // 发送消息, 修改ap_article_config表中is_down字段为1
        NewsSyncDto newsSyncDto = new NewsSyncDto();
        // 获取article_id
        newsSyncDto.setArticleId(wmNews.getArticleId());
        newsSyncDto.setDown(wmNews.getEnable() != 1);
        // 将对象转为字符串
        String newsSyncDtoJson = JSON.toJSONString(newsSyncDto);
        // TODO 发送消息
        kafkaTemplate.send("EnableAsyncTopic", newsSyncDtoJson);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 解析文章数据
     * @Param [content]
     * @Return {@link List<String>}
     */
    private List<String> parseArticleData(String content) {
        List<String> contentsImage = new ArrayList<>();
        // 将json数据转化为集合
        List<Content> contents = JSON.parseArray(content, Content.class);
        // 遍历集合
        for (Content contentImage : contents) {
            // 判断是否是image属性
            if (contentImage.getType().equals("image")) {
                // 获取value属性
                String contentValue = contentImage.getValue();
                // 将image存入集合
                contentsImage.add(contentValue);
            }
        }
        // 返回集合
        return contentsImage;
    }

    /**
     * 查看文章列表
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult viewArticleList(NewsAuthDto newsAuthDto) {
        // 校验数据
        if (newsAuthDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 分页器
        IPage<WmNews> pageInfo = new Page<>(newsAuthDto.getPage(), newsAuthDto.getSize());
        // 构建查询条件
        LambdaQueryWrapper<WmNews> queryWrapper = new LambdaQueryWrapper<>();
        // 判断是否传回了title
        if (StringUtils.isNoneBlank(newsAuthDto.getTitle())) {
            queryWrapper.like(WmNews::getTitle, newsAuthDto.getTitle());
        }
        // 按照创建时间进行降序查询
        queryWrapper.orderByDesc(WmNews::getCreatedTime);

        // 按照状态查询
        if (newsAuthDto.getStatus() != null) {
            queryWrapper.eq(WmNews::getStatus, newsAuthDto.getStatus());
        }
        // 分页查询
        wmNewsService.page(pageInfo, queryWrapper);
        // 查询文章数据
        List<WmNews> wmNewsList = pageInfo.getRecords();

        // 创建map存放WmNews
        Map<String, List<WmNews>> wmNewsMap = new HashMap<>();
        // 存入WmNews
        wmNewsMap.put("wmNews", wmNewsList);

        // TODO 调用feign
        ResponseResult responseResult = articleFeignClient.articleAuthorFeignClient(wmNewsMap);

        // 封装返回数据
        PageResponseResult pageResponseResult = new PageResponseResult();
        pageResponseResult.setCurrentPage((int) pageInfo.getCurrent());
        pageResponseResult.setSize((int) pageInfo.getSize());
        pageResponseResult.setTotal((int) pageInfo.getTotal());
        // TODO 返回wmNewsAuthorDto
        pageResponseResult.setData(responseResult.getData());
        return pageResponseResult;
    }

    /**
     * 查看文章详情
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult viewArticleDetails(Integer id) {
        // 校验参数
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 根据id查询
        WmNews wmNews = wmNewsService.getById(id);
        WmNewsAuthorDto wmNewsAuthorDto = new WmNewsAuthorDto();
        // 拷贝数据
        BeanUtils.copyProperties(wmNews, wmNewsAuthorDto);
        // TODO 获取作者名
        WmUser wmUser = wmUserService.getById(wmNewsAuthorDto.getUserId());
        if (wmUser != null) {
            // 设置作者名
            wmNewsAuthorDto.setAuthorName(wmUser.getName());
        }
        // 返回wmNewsAuthorDto
        return ResponseResult.okResult(wmNewsAuthorDto);
    }

    /**
     * 驳回文章审核
     * @Param [newsAuthDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult rejectArticleReview(NewsAuthDto newsAuthDto) {
        // 校验参数
        if (newsAuthDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 驳回理由不能为空
        if (newsAuthDto.getMsg() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "驳回理由不能为空");
        }
        // 根据id查询
        WmNews wmNewsOne = wmNewsService.getById(newsAuthDto.getId());
        WmNews wmNews = new WmNews();
        // 拷贝数据
        BeanUtils.copyProperties(wmNewsOne, wmNews);
        // 原因
        wmNews.setReason(newsAuthDto.getMsg());
        // 修改状态
        wmNews.setStatus(newsAuthDto.getStatus().shortValue());
        // 修改
        wmNewsService.updateById(wmNews);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 文章审核通过
     * @Param [newsAuthDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult articlePassedTheReview(NewsAuthDto newsAuthDto) {
        // 校验参数
        if (newsAuthDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 根据id查询文章
        WmNews wmNewsOne = wmNewsService.getById(newsAuthDto.getId());
        // 拷贝数据
        ArticleFeignDto articleFeignDto = new ArticleFeignDto();
        BeanUtils.copyProperties(wmNewsOne, articleFeignDto);
        // 补全字段
        articleContentReviewService.fillField(wmNewsOne, articleFeignDto);
        // APP端文章保存
        articleFeignClient.articleFeignClient(articleFeignDto);
        // 调用方法, 发布文章
        articleContentReviewService.postArticle(articleFeignDto);
        // 返回数据
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
