package com.heima.model.behavior.dtos;

import lombok.Data;

/**
 * @ClassName BehaviorUnLikeDto.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */

@Data
public class BehaviorUnLikeDto {

    private Long articleId; // 文章id

    private Integer type; // 0不喜欢, 1取消不喜欢
}
