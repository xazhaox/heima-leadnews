package com.heima.behavior.service.impl;

import com.heima.behavior.service.BehaviorFeignService;
import com.heima.model.common.constants.BehaviorConstants;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @ClassName BehaviorFeignServiceImpl.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class BehaviorFeignServiceImpl implements BehaviorFeignService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 回显用户行为
     * @Param [articleId, id]
     * @Return {@link ResponseResult<Boolean>}
     */
    @Override
    public ResponseResult<Boolean> loadUserBehavior(Long articleId, Integer userId) {
        // 构建key
        String hashKey = BehaviorConstants.LIKE_KEY + ":" + String.valueOf(articleId);
        // 查询redis
        Boolean isLike = stringRedisTemplate.opsForHash().hasKey(hashKey, String.valueOf(userId));
        // 返回结果
        return ResponseResult.okResult(isLike);
    }
}
