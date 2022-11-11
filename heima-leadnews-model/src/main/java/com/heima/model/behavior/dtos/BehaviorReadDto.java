package com.heima.model.behavior.dtos;

import lombok.Data;

/**
 * @ClassName BehaviorReadDto.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */

@Data
public class BehaviorReadDto {

    private Long articleId; // 文章id

    private Integer count; // 阅读次数
}
