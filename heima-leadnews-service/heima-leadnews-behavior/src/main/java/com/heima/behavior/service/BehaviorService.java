package com.heima.behavior.service;

import com.heima.model.behavior.dtos.ArticleInfoDto;
import com.heima.model.behavior.dtos.BehaviorLikeDto;
import com.heima.model.behavior.dtos.BehaviorReadDto;
import com.heima.model.behavior.dtos.BehaviorUnLikeDto;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @ClassName BehaviorService.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */
public interface BehaviorService {

    /**
     * 点赞或取消点赞
     * @Param [behaviorLikeDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult likeOrUnlike(BehaviorLikeDto behaviorLikeDto);

    /**
     * 阅读次数
     * @Param [behaviorReadDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult numberOfTimesRead(BehaviorReadDto behaviorReadDto);

    /**
     * 不喜欢或取消不喜欢
     * @Param [behaviorUnLikeDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult unLikesBehavior(BehaviorUnLikeDto behaviorUnLikeDto);
}
