package com.heima.feign.behavior;

import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName BehaviorFeignClient.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */

// 服务提供方的服务名称
@FeignClient(name = "leadnews-behavior")
public interface BehaviorFeignClient {

    /**
     * 回显用户行为
     * @Param [articleId, id]
     * @Return {@link ResponseResult <Boolean>}
     */
    @PostMapping("/behavior/load_article_behavior")
    public ResponseResult<Boolean> loadUserBehavior(@RequestParam("articleId") Long articleId,
                                                    @RequestParam("userId") Integer userId);
}
