package com.heima.model.user.dtos;

import lombok.Data;

/**
 * @ClassName UserRelationDto.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */

@Data
public class UserRelationDto {

    private Long articleId; // 文章id

    private Integer authorId; // 作者id

    private Integer operation; // 0关注, 1取消

}
