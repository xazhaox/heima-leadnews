package com.heima.behavior.service.impl;

import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.heima.behavior.service.ComputingArticlesService;
import com.heima.feign.article.ArticleFeignClient;
import com.heima.feign.wemedia.WemediaFeignClient;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.constants.TaskConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ComputingArticlesServiceImpl.java
 * @Author xazhao
 * @Create 2022.09.02
 * @UpdateUser
 * @UpdateDate 2022.09.02
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class ComputingArticlesServiceImpl implements ComputingArticlesService {

    @Autowired
    private ArticleFeignClient articleFeignClient;

    @Autowired
    private WemediaFeignClient wemediaFeignClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static void main(String[] args) {

        Date addYearsDate = DateUtils.addYears(new Date(), -1);
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(addYearsDate);
        System.out.println(dateFormat);
    }

    /**
     * 定时计算热点文章
     * @Param []
     * @Return
     */
    @Override
    public void computingHotArticlesTask() {
        // 构建时间, 一年以内
        Date addYearsDate = DateUtils.addYears(new Date(), -1);
        // 调用feign, 查询指定时间内的文章
        ResponseResult<List<ApArticle>> responseResult = articleFeignClient.findArticleByPublishTime(addYearsDate);
        // 判断远程调用成功
        if (responseResult.getCode() == AppHttpCodeEnum.SUCCESS.getCode()) {
            // 获取查询出的文章数据
            List<ApArticle> apArticles = responseResult.getData();
            // 遍历
            for (ApArticle apArticle : apArticles) {
                // 按照行为数据及对应的权重计算每篇文章的分值
                Integer articleSocre = getArticleSocre(apArticle);
                // 设置分值
                apArticle.setScore(articleSocre);
            }
            // 利用Collections工具类对分数降序排序
            Collections.sort(apArticles, new Comparator<ApArticle>() {
                @Override
                public int compare(ApArticle o1, ApArticle o2) {
                    // 降序
                    return -(o1.getScore() - o2.getScore());
                }
            });
            // 将每个频道分值最高的30条文章数据过滤出来, 调用feign查询频道
            ResponseResult<List<WmChannel>> result = wemediaFeignClient.channels();
            // 判断调用成功
            if (result.getCode() == AppHttpCodeEnum.SUCCESS.getCode()) {
                // 获取频道数据
                List<WmChannel> wmChannels = result.getData();
                // 遍历每个频道
                for (WmChannel wmChannel : wmChannels) {
                    // 获取文章id
                    Integer wmChannelId = wmChannel.getId();
                    // 过滤每个频道分值最高的limit(5)5条文章
                    List<ApArticle> apArticleList = apArticles.stream().filter((t) -> {
                        // 比较ApArticle中的ChannelId和频道id相同的
                        return t.getChannelId().equals(wmChannelId);
                    }).limit(5).collect(Collectors.toList());
                    // 构建key
                    String zSetKey = TaskConstants.HOT_KEY + ":" + wmChannelId;
                    // 将过滤出的文章缓存到redis
                    for (ApArticle apArticle : apArticleList) {
                        stringRedisTemplate.opsForZSet().add(zSetKey, String.valueOf(apArticle.getId()), apArticle.getScore());
                    }
                }
                // 处理推荐频道, 过滤出所有频道中分值最高的10条数据
                List<ApArticle> apArticlesList = apArticles.stream().limit(10).collect(Collectors.toList());
                // 遍历得到的10条数据
                for (ApArticle apArticle : apArticlesList) {
                    // 构建key
                    String zSetKey = TaskConstants.HOT_KEY + ":__all__";
                    // 存入redis
                    stringRedisTemplate.opsForZSet().add(zSetKey, String.valueOf(apArticle.getId()), apArticle.getScore());
                }
            }
        }
    }

    /**
     * 按照行为数据及对应的权重计算每篇文章的分值
     * @Param [apArticle]
     * @Return {@link java.lang.Integer}
     */
    private Integer getArticleSocre(ApArticle apArticle) {
        // 计算分值
        return apArticle.getLikes() * 3 + apArticle.getCollection() * 8
                + apArticle.getComment() * 5 + apArticle.getViews();
    }
}
