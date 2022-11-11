package com.heima.model.article.dtos;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName ArticleHomeDto.java
 * @Author xazhao
 * @Create 2022.08.18
 * @Description
 */

@Data
public class ArticleHomeDto {

    private Date maxBehotTime; // 最大时间

    private Date minBehotTime; // 最小时间

    private Integer size; // 分页size

    private String tag; // 频道ID

}
