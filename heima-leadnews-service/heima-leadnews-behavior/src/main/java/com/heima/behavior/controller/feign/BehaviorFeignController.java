package com.heima.behavior.controller.feign;

import com.heima.behavior.service.BehaviorFeignService;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName BehaviorFeignController.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */

@RestController
@RequestMapping("/behavior")
public class BehaviorFeignController {

    @Autowired
    private BehaviorFeignService behaviorFeignService;

    /**
     * 回显用户行为
     * @Param [articleId, id]
     * @Return {@link ResponseResult<Boolean>}
     */
    @PostMapping("/load_article_behavior")
    public ResponseResult<Boolean> loadUserBehavior(@RequestParam("articleId") Long articleId,
                                                    @RequestParam("userId") Integer userId) {

        return behaviorFeignService.loadUserBehavior(articleId, userId);
    }
}
