package com.heima.behavior.service;

import com.heima.model.common.dtos.ResponseResult;

/**
 * @ClassName BehaviorFeignService.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */
public interface BehaviorFeignService {

    /**
     * 回显用户行为
     * @Param [articleId, id]
     * @Return {@link ResponseResult<Boolean>}
     */
    ResponseResult<Boolean> loadUserBehavior(Long articleId, Integer userId);

}
