package com.heima.search.service.impl;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.dtos.SearchDto;
import com.heima.model.search.pojos.ApAssociateWords;
import com.heima.search.service.KeywordAssociationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName KeywordAssociationServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class KeywordAssociationServiceImpl implements KeywordAssociationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 自动补全查询
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult keywordAssociation(SearchDto searchDto) {
        // 校验参数
        if (searchDto == null || StringUtils.isBlank(searchDto.getSearchWords())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 分页检查
        if (searchDto.getPageSize() > 20) {
            searchDto.setPageSize(20);
        }
        // 构建查询条件
        Query query = Query.query(Criteria.where("associateWords")
                .regex(".*?\\" + searchDto.getSearchWords() + ".*"));
        query.limit(searchDto.getPageSize());
        // 查询
        List<ApAssociateWords> apAssociateWords = mongoTemplate.find(query, ApAssociateWords.class);
        // 返回数据
        return ResponseResult.okResult(apAssociateWords);
    }
}
