package com.heima.behavior.xxLJob;

import com.heima.behavior.service.ComputingArticlesService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName HotArticleTimingTasks.java
 * @Author xazhao
 * @Create 2022.09.02
 * @UpdateUser
 * @UpdateDate 2022.09.02
 * @Description
 * @Version 1.0.0
 */

@Component
public class HotArticleTimingTasks {

    @Autowired
    private ComputingArticlesService computingArticlesService;

    /**
     * 定时计算热点文章
     * @Param []
     * @Return
     */
    @XxlJob("leadnewsJobHandler")
    public void demoJobHandler() throws Exception {
        // 调用方法, 执行定时任务
        computingArticlesService.computingHotArticlesTask();
    }
}
