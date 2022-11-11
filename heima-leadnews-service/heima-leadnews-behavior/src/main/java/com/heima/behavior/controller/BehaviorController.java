package com.heima.behavior.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.heima.behavior.service.BehaviorService;
import com.heima.model.behavior.dtos.ArticleInfoDto;
import com.heima.model.behavior.dtos.BehaviorLikeDto;
import com.heima.model.behavior.dtos.BehaviorReadDto;
import com.heima.model.behavior.dtos.BehaviorUnLikeDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName BehaviorController.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */

@RestController
@RequestMapping("/api/v1")
public class BehaviorController {

    @Autowired
    private BehaviorService behaviorService;

    /**
     * 点赞或取消点赞
     * @Param [behaviorLikeDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/likes_behavior")
    public ResponseResult likeOrUnlike(@RequestBody BehaviorLikeDto behaviorLikeDto) {

        return behaviorService.likeOrUnlike(behaviorLikeDto);
    }

    /**
     * 阅读次数
     * @Param [behaviorReadDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/read_behavior")
    public ResponseResult numberOfTimesRead(@RequestBody BehaviorReadDto behaviorReadDto) {

        return behaviorService.numberOfTimesRead(behaviorReadDto);
    }

    /**
     * 不喜欢或取消不喜欢
     * @Param [behaviorUnLikeDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/un_likes_behavior")
    public ResponseResult unLikesBehavior(@RequestBody BehaviorUnLikeDto behaviorUnLikeDto) {

        return behaviorService.unLikesBehavior(behaviorUnLikeDto);
    }
}
