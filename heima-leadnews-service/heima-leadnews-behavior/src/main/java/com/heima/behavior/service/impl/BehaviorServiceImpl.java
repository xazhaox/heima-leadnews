package com.heima.behavior.service.impl;

import com.heima.behavior.service.BehaviorService;
import com.heima.model.behavior.dtos.BehaviorLikeDto;
import com.heima.model.behavior.dtos.BehaviorReadDto;
import com.heima.model.behavior.dtos.BehaviorUnLikeDto;
import com.heima.model.common.constants.BehaviorConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.common.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @ClassName BehaviorServiceImpl.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class BehaviorServiceImpl implements BehaviorService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 点赞或取消点赞
     * @Param [behaviorLikeDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult likeOrUnlike(BehaviorLikeDto behaviorLikeDto) {
        // 获取用户id
        Integer userId = ThreadLocalUtils.getUserId();
        // 判断是否是游客登录
        if (userId == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 构建key
        String hashKey = BehaviorConstants.LIKE_KEY + ":" + String.valueOf(behaviorLikeDto.getArticleId());
        // 用户, 用户点赞
        if (behaviorLikeDto.getOperation() == 0) {
            // 存储redis
            stringRedisTemplate.opsForHash().put(hashKey, String.valueOf(userId), "8");
        } else {
            // 用户取消点赞
            stringRedisTemplate.opsForHash().delete(hashKey, String.valueOf(userId));
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 阅读次数
     * @Param [behaviorReadDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult numberOfTimesRead(BehaviorReadDto behaviorReadDto) {
        // 获取用户id
        Integer userId = ThreadLocalUtils.getUserId();
        // 构建key
        String hashKey = BehaviorConstants.READ_KEY + ":" + String.valueOf(behaviorReadDto.getArticleId());
        // 存储redis
        stringRedisTemplate.opsForHash().increment(hashKey, String.valueOf(userId), 1);
        // 返回数据
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 不喜欢或取消不喜欢
     * @Param [behaviorUnLikeDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult unLikesBehavior(BehaviorUnLikeDto behaviorUnLikeDto) {
        // 获取用户id
        Integer userId = ThreadLocalUtils.getUserId();
        // 构建key
        String hashKey = BehaviorConstants.UN_LIKE_KEY + ":" + String.valueOf(behaviorUnLikeDto.getArticleId());
        // 判断是否是游客登录
        if (userId == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 用户登录操做不喜欢
        if (behaviorUnLikeDto.getType() == 0) {
            // 存储redis
            stringRedisTemplate.opsForHash().put(hashKey, String.valueOf(userId), String.valueOf(behaviorUnLikeDto.getType()));
        } else {
            // 用户取消不喜欢
            stringRedisTemplate.opsForHash().delete(hashKey, String.valueOf(userId));
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
