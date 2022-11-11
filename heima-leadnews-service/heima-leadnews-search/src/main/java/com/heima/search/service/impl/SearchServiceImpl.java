package com.heima.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.dataimport.pojos.EsArticle;
import com.heima.model.search.dtos.SearchDto;
import com.heima.model.search.pojos.ApUserSearch;
import com.heima.search.service.SaveSearchHistoryService;
import com.heima.search.service.SearchService;
import com.heima.utils.common.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SearchServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.27
 * @UpdateUser
 * @UpdateDate 2022.08.27
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private SaveSearchHistoryService saveSearchHistoryService;

    /**
     * 搜索功能
     * @Param [searchDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult search(SearchDto searchDto) throws IOException {
        // 创建SearchRequest
        SearchRequest searchRequest = new SearchRequest("app_info_article");
        // 获取搜索关键字
        String searchWords = searchDto.getSearchWords();

        // TODO 保存搜索历史
        log.info(ThreadLocalUtils.getUserId().toString());
        // 判断是否为游客登录
        if (ThreadLocalUtils.getUserId() != 0) {
            ApUserSearch apUserSearch = new ApUserSearch();
            apUserSearch.setId(null);
            apUserSearch.setKeyword(searchWords);
            apUserSearch.setUserId(ThreadLocalUtils.getUserId());
            apUserSearch.setCreatedTime(new Date());
            // 保存ap_user_search
            saveSearchHistoryService.saveSearchHistory(apUserSearch);
        }

        // 构建BoolQueryBuilder, 组装查询条件
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        // 判断关键字为空, 查询所有
        if (searchWords == null || "".equals(searchWords)) {
            // 查询所有
            boolQuery.must(QueryBuilders.matchAllQuery());
        } else {
            // 按"title", "content"关键字查询
            MultiMatchQueryBuilder keySearchWords = QueryBuilders
                    .multiMatchQuery(searchDto.getSearchWords(), "title", "content");
            // 组装条件
            boolQuery.must(keySearchWords);

            // TODO 将搜索的关键字高亮显示
            searchRequest.source()
                    .highlighter(new HighlightBuilder()
                            // 高亮显示的字段
                            .field("title")
                            // 高亮显示字段的前缀
                            .preTags("<em style=\"color: red;font-size: 20px\">")
                            // 高亮显示字段的后缀
                            .postTags("</em>")
                    );

            // 判断传回的时间是否不为空且小于当前时间
            if (searchDto.getMinBehotTime() != null && searchDto.getMinBehotTime().before(new Date())) {
                // 上滑加载更多
                RangeQueryBuilder publishTime = QueryBuilders
                        .rangeQuery("publishTime").lt(searchDto.getMinBehotTime());
                // 组装条件
                boolQuery.filter(publishTime);
            }
        }
        // 构建查询条件
        searchRequest.source().query(boolQuery);

        // 分页
        searchRequest.source()
                // 当前页
                // .from((searchDto.getPageNum() - 1) * searchDto.getPageSize())
                .from(0)
                // 每页显示的条数, 传回的getPageSize = 20
                .size(searchDto.getPageSize());

        // 排序
        searchRequest.source()
                // 根据publishTime字段, 降序
                .sort("publishTime", SortOrder.DESC);

        // TODO 高亮
        HighlightBuilder highlightBuiler = new HighlightBuilder()
                // 高亮显示的字段
                .field("title")
                // 高亮显示字段的前缀
                .preTags("<em style=\"color: red;font-size: 20px\">")
                // 高亮显示字段的后缀
                .postTags("</em>");
        // 构建条件
        searchRequest.source().highlighter(highlightBuiler);

        // 发送请求
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 调用方法, 解析searchResponse数据
        List<EsArticle> esArticleList = responseDataAnalysis(searchResponse);
        // 返回数据
        return ResponseResult.okResult(esArticleList);
    }

    /**
     * 解析返回的数据
     * @Param [searchResponse]
     * @Return {@link List<EsArticle>}
     */
    private List<EsArticle> responseDataAnalysis(SearchResponse searchResponse) {
        // 获取文档数据, hits数组
        SearchHit[] searchHitsHits = searchResponse.getHits().getHits();
        // 创建集合存放EsArticle对象
        List<EsArticle> esArticleList = new ArrayList<>();
        // 遍历数组
        for (SearchHit searchHitsHit : searchHitsHits) {
            // 获取source部分, 获取到的数据为json数据
            String sourceAsString = searchHitsHit.getSourceAsString();
            // 将获取到的json数据转为EsArticle对象
            EsArticle esArticle = JSON.parseObject(sourceAsString, EsArticle.class);
            // TODO 解析高亮部分, 获取高亮结果
            Map<String, HighlightField> highlightFields = searchHitsHit.getHighlightFields();
            // 判断高亮的部分是否为空
            if (!CollectionUtils.isEmpty(highlightFields)) {
                // 根据字段名获取高亮结果
                HighlightField title = highlightFields.get("title");
                // 判断高亮结果是否为空
                if (title != null) {
                    // 获取高亮值
                    String highlightTitle = title.getFragments()[0].string();
                    // 覆盖高亮结果
                    esArticle.setTitle(highlightTitle);
                }
            }
            // 封装到集合
            esArticleList.add(esArticle);
        }
        return esArticleList;
    }
}
