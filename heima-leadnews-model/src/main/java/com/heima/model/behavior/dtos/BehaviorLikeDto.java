package com.heima.model.behavior.dtos;

import lombok.Data;

/**
 * @ClassName BehaviorDto.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */

@Data
public class BehaviorLikeDto {

    private Long articleId; // 文章id

    private Integer operation; // 0点赞, 1取消点赞

    private Integer type; // 0文章, 1动态, 2评论
}
