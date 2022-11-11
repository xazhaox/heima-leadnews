package com.heima.behavior.service;

/**
 * @ClassName ComputingArticlesService.java
 * @Author xazhao
 * @Create 2022.09.02
 * @UpdateUser
 * @UpdateDate 2022.09.02
 * @Description
 * @Version 1.0.0
 */
public interface ComputingArticlesService {

    /**
     * 定时计算热点文章
     * @Param []
     * @Return
     */
    public void computingHotArticlesTask();
}
