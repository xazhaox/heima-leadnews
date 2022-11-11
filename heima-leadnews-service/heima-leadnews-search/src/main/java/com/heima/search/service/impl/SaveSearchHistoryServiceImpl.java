package com.heima.search.service.impl;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.pojos.ApUserSearch;
import com.heima.search.service.SaveSearchHistoryService;
import com.heima.utils.common.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SaveSearchHistoryServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class SaveSearchHistoryServiceImpl implements SaveSearchHistoryService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存搜索历史记录
     * @Param [apUserSearch]
     * @Return
     */
    @Override
    @Async
    public void saveSearchHistory(ApUserSearch apUserSearch) {
        // 构建查询条件
        Query query = Query.query(Criteria.where("userId").is(apUserSearch.getUserId())
                .and("keyword").is(apUserSearch.getKeyword()));
        // 查询搜索记录
        ApUserSearch userSearch = mongoTemplate.findOne(query, ApUserSearch.class);
        // 判断是否已经保存
        if (userSearch != null) {
            // 不为空, 已经保存, 修改创建时间
            userSearch.setCreatedTime(new Date());
            // 保存
            mongoTemplate.save(userSearch);
        } else {
            // 为空, 保存前端传回的数据
            mongoTemplate.save(apUserSearch);
        }
    }

    /**
     * 查询搜索记录
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult querySearchRecords(Map<String, Integer> searchSize) {
        // 获取userId
        Integer userId = ThreadLocalUtils.getUserId();
        // 构建条件
        Query query= Query.query(Criteria.where("userId").is(userId));
        // 排序
        query.with(Sort.by(Sort.Order.desc("createdTime")));
        // 获取pageSize
        Integer pageSize = searchSize.get("pageSize");
        // 限制显示的条数
        query.limit(pageSize);
        // 查询
        List<ApUserSearch> apUserSearches = mongoTemplate.find(query, ApUserSearch.class);
        // 返回数据
        return ResponseResult.okResult(apUserSearches);
    }

    /**
     * 删除搜索记录
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult deleteSearchRecords(Map<String, String> apUserSearchId) {
        // 获取id
        String id = apUserSearchId.get("id");
        // 获取userId
        Integer userId = ThreadLocalUtils.getUserId();
        // 构建条件
        Query query = Query.query(Criteria.where("id").is(id)
                .and("userId").is(userId));
        // 删除
        mongoTemplate.remove(query, ApUserSearch.class);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
