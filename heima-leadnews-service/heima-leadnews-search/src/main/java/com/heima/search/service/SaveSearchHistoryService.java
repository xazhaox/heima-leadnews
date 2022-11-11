package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.pojos.ApUserSearch;

import java.util.Map;

/**
 * @ClassName SaveSearchHistoryService.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */
public interface SaveSearchHistoryService {

    /**
     * 保存搜索历史记录
     * @Param [apUserSearch]
     * @Return
     */
    public void saveSearchHistory(ApUserSearch apUserSearch);

    /**
     * 查询搜索记录
     * @Param []
     * @Return {@link ResponseResult}
     */
    public ResponseResult querySearchRecords(Map<String, Integer> searchSize);

    /**
     * 删除搜索记录
     * @Param []
     * @Return {@link ResponseResult}
     */
    public ResponseResult deleteSearchRecords(Map<String, String> apUserSearchId);
}
