package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.feign.article.ArticleFeignClient;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ArticleFeignDto;
import com.heima.model.common.constants.WmNewsConstant;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.dataimport.pojos.EsArticle;
import com.heima.model.wemedia.dtos.Content;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.common.SensitiveWordUtil;
import com.heima.wemedia.config.RabbitMqConfig;
import com.heima.wemedia.service.*;
import javafx.beans.binding.SetExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ArticleContentReviewServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.22
 * @Description
 */

@Service
public class ArticleContentReviewServiceImpl implements ArticleContentReviewService {

    @Autowired
    private GreenImageScan greenImageScan;

    @Autowired
    private GreenTextScan greenTextScan;

    @Autowired
    private WmNewsService wmNewsService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ArticleFeignClient articleFeignClient;

    @Autowired
    private WmChannelService wmChannelService;

    @Autowired
    private WmUserService wmUserService;

    @Autowired
    private WmSensitiveService wmSensitiveService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 初始化方法, 初始化字典树, 只需要初始化一次
     * @Param []
     * @Return 
     */
    @PostConstruct
    public void initSensitives() {
        LambdaQueryWrapper<WmSensitive> queryWrapper = new LambdaQueryWrapper<>();
        // 查询wm_sensitive表
        List<WmSensitive> wmSensitiveList = wmSensitiveService.list(queryWrapper);
        // 遍历获取sensitives字段的值, t表示从集合中遍历出的元素
        List<String> sensitivesList = wmSensitiveList.stream().map((t) -> {
            // 获取sensitives字段的值
            return t.getSensitives();
        }).collect(Collectors.toList());
        // 调用SensitiveWordUtil工具类中的initMap(), 初始化字典树
        SensitiveWordUtil.initMap(sensitivesList);
    }

    /**
     * 审核文章内容
     * @Param [wmNews]
     * @Return
     */
    @Override
    public void articleContentReview(WmNews wmNews) throws Exception {
        // 获取文章内容
        String newsContent = wmNews.getContent();
        // 将文章内容反序列化为集合
        List<Content> contentList = JSON.parseArray(newsContent, Content.class);
        // 定义StringBuilder存放文章待审核文本内容
        StringBuilder contentText = new StringBuilder();
        // 定义set集合存放文章待审核图片内容, set集合不重复若多张图片相同减少审核次数
        Set<String> contentImages = new HashSet<>();
        // 遍历集合, 解析文章中内容文本和图片
        for (Content content : contentList) {
            // 判断是否是图片内容, type类型, value内容
            if ("image".equals(content.getType())) {
                // 图片内容存入contentImages集合
                contentImages.add(content.getValue());
            } else {
                // 文本内容存入contentText
                contentText.append(content.getValue());
            }
        }
        // 获取封面图片的url
        String newsImages = wmNews.getImages();
        // 判断封面图片是否为空或空字符串
        if (StringUtils.isNoneBlank(newsImages)) {
            // 根据逗号切割图片url
            String[] splitImages = newsImages.split(",");
            // 将封面图片存入待审核的图片集合中
            Collections.addAll(contentImages, splitImages);
        }

        // TODO 基于自维护的敏感词进行搜索, 初始化字典树initSensitives()
        // 传入文章的文本信息基于字典树进行搜索
        Map<String, Integer> sensitivesMap = SensitiveWordUtil.matchWords(contentText.toString());
        // 搜索完成之后map集合不为空, 表示存在敏感词, 审核未通过
        if (!sensitivesMap.isEmpty()) {
            // 未通过, 将状态修改为2
            wmNews.setStatus(WmNews.Status.FAIL.getCode());
            // label为文本垃圾检测结果的分类
            wmNews.setReason("违规信息为 : " + sensitivesMap.keySet());
            // 修改数据库
            wmNewsService.updateById(wmNews);
            return;
        }

        // 将文本内容发送给阿里云内容安全服务进行审核
        boolean textReview = textReview(wmNews, contentText);
        // 判断textReview, false为未通过
        if (!textReview) {
            // 结束方法
            return;
        }
        // 将图片发送给阿里云内容安全服务进行审核
        boolean imagesReview = imagesReview(wmNews, contentImages);
        // 判断imagesReview, false为未通过
        if (!imagesReview) {
            // 结束方法
            return;
        }
        // 获取发布时间
        Date publishTime = wmNews.getPublishTime();
        // 审核通过, 判断文章的发布时间小于等于当前时间
        if (publishTime.after(new Date())) {
            // 修改文章状态为8, 审核通过, 待发布
            wmNews.setStatus(WmNews.Status.SUCCESS.getCode());
            wmNews.setReason("审核通过, 待发布");
            // 修改wm_news表
            wmNewsService.updateById(wmNews);

            // 计算消息的过期时间, 发布时间的时间戳(毫秒值)getTime() - 当前时间的时间戳(毫秒值)
            long ttl = wmNews.getPublishTime().getTime() - System.currentTimeMillis();
            // 构建消息体
            Message message = MessageBuilder.withBody(
                    // 发送wmNews的id
                    String.valueOf(wmNews.getId()).getBytes())
                    // 消息的过期时间
                    .setExpiration(String.valueOf(ttl))
                    .build();
            // TODO 发送消息
            rabbitTemplate.convertAndSend(
                    // 发送到普通交换机到队列, 监听死信队列
                    RabbitMqConfig.SEND_EXCHANGE,
                    RabbitMqConfig.SEND_KEY,
                    // 消息
                    message
            );

        } else {
            // TODO 发布文章, 远程调用feign将自媒体文章信息保存article文章表
            postArticle(wmNews);
        }
        // 生成文章详情页面, 保存minio, 在ApArticleServiceImpl实现类中
    }

    /**
     * 发布文章
     * @Param [wmNews]
     * @Return
     */
    @Override
    public void postArticle(WmNews wmNews) {
        // 发布文章, 远程调用feign将自媒体文章信息保存article文章表
        ArticleFeignDto articleFeignDto = new ArticleFeignDto();
        // 拷贝wmNews中的数据到articleFeignDto中
        BeanUtils.copyProperties(wmNews, articleFeignDto);
        // 填充字段
        fillField(wmNews, articleFeignDto);

        // 远程调用feign将自媒体文章信息保存article文章表
        ResponseResult<Map<String, String>> responseResult = articleFeignClient.articleFeignClient(articleFeignDto);
        // 修改文章状态为9, 发布成功
        wmNews.setStatus(WmNews.Status.PUBLISHED.getCode());
        wmNews.setReason("已发布成功");
        // 获取map
        Map<String, String> resultData = responseResult.getData();
        // 获取article_id
        long articleId = Long.parseLong(resultData.get("articleId"));
        // 设置wm_news中的article_id, article_id为调用feign返回的id
        wmNews.setArticleId(articleId);
        // 修改wm_news表
        wmNewsService.updateById(wmNews);
        // TODO 数据同步, 发送消息将审核通过的文章同步到es
        EsArticle esArticle = new EsArticle();
        esArticle.setId(articleId);
        esArticle.setTitle(wmNews.getTitle());
        esArticle.setContent(wmNews.getContent());
        esArticle.setAuthorId(wmNews.getUserId().longValue());
        // 获取author_id
        Integer userId = wmNews.getUserId();
        // 查询wm_user表
        WmUser wmUser = wmUserService.getById(userId);
        esArticle.setAuthorName(wmUser.getName());
        esArticle.setImages(wmNews.getImages());
        esArticle.setLayout(wmNews.getType().intValue());
        esArticle.setPublishTime(wmNews.getPublishTime());
        // 获取静态地址
        String staticUrl = resultData.get("staticUrl");
        esArticle.setStaticUrl(staticUrl);
        // 发送消息, 将对象转为json字符串发送
        kafkaTemplate.send(WmNewsConstant.ARTICLE_ASYNC_ES_TOPIC, JSON.toJSONString(esArticle));
    }


    /**
     * 填充字段
     * @Param [wmNews, articleFeignDto]
     * @Return
     */
    public void fillField(WmNews wmNews, ArticleFeignDto articleFeignDto) {
        // 获取channel_id
        Integer channelId = wmNews.getChannelId();
        // 查询wm_channel表
        WmChannel wmChannel = wmChannelService.getById(channelId);
        // 设置channelName
        articleFeignDto.setChannelName(wmChannel.getName());

        // 获取author_id
        Integer userId = wmNews.getUserId();
        // 查询wm_user表
        WmUser wmUser = wmUserService.getById(userId);
        // 设置authorName
        articleFeignDto.setAuthorName(wmUser.getName());
    }

    /**
     * 图片审核
     * @Param [wmNews, contentImages]
     * @Return {@link boolean}
     */
    private boolean imagesReview(WmNews wmNews, Set<String> contentImages) throws Exception {
        // 定义存放byte[]的集合
        List<byte[]> byteContentImages = new ArrayList<>();
        // 遍历存放待审核图片的集合
        for (String contentImageUrl : contentImages) {
            // 从minio中下载图片
            byte[] bytesImages = fileStorageService.downLoadFile(contentImageUrl);
            // 存入集合中
            byteContentImages.add(bytesImages);
        }
        Map<String, String> imageScanMap = greenImageScan.imageScan(byteContentImages);
        // 判断若内容和封面中没有图片
        if (imageScanMap == null) {
            // 返回true, 无需进行图片审核
            return true;
        }
        // 获取suggestion, 表示执行的后续操作, pass通过
        String suggestion = imageScanMap.get("suggestion");
        // 判断suggestion的值是否为pass
        if (!suggestion.equals("pass")) {
            // 判断suggestion的值是否为block
            if (suggestion.equals("block")) {
                // 未通过, 将状态修改为2
                wmNews.setStatus(WmNews.Status.FAIL.getCode());
                // label为文本垃圾检测结果的分类
                wmNews.setReason("违规信息为 : " + imageScanMap.get("label"));
            } else {
                // 需要人工审核, 将状态修改为3
                wmNews.setStatus(WmNews.Status.ADMIN_AUTH.getCode());
                // label为文本垃圾检测结果的分类
                wmNews.setReason("文章内容存在异常, 需要转人工审核");
            }
            // 修改wm_news表
            wmNewsService.updateById(wmNews);
            return false;
        }
        return true;
    }

    /**
     * 文本审核
     * @Param [wmNews, contentText]
     * @Return {@link boolean}
     */
    private boolean textReview(WmNews wmNews, StringBuilder contentText) throws Exception {
        Map<String, String> textScanMap = greenTextScan.greeTextScan(contentText.toString());
        // 获取suggestion, 表示执行的后续操作, pass通过
        String suggestion = textScanMap.get("suggestion");
        // 判断suggestion的值是否为pass
        if (!suggestion.equals("pass")) {
            // 判断suggestion的值是否为block
            if (suggestion.equals("block")) {
                // 不为pass未通过, 将状态修改为2
                wmNews.setStatus(WmNews.Status.FAIL.getCode());
                // label为文本垃圾检测结果的分类
                wmNews.setReason("违规信息为 : " + textScanMap.get("label"));
            } else if (suggestion.equals("review")){
                // 需要人工审核, 将状态修改为3
                wmNews.setStatus(WmNews.Status.ADMIN_AUTH.getCode());
                // label为文本垃圾检测结果的分类
                wmNews.setReason("文章内容存在异常, 需要转人工审核");
            }
            // 修改wm_news表
            wmNewsService.updateById(wmNews);
            return false;
        }
        return true;
    }
}
