package com.heima.model.behavior.dtos;

import lombok.Data;

/**
 * @ClassName ArticleInfoDto.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */

@Data
public class ArticleInfoDto {

    private Long articleId; // 文章id

    private Integer authorId; // 作者id
}
