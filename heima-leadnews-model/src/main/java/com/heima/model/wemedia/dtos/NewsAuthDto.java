package com.heima.model.wemedia.dtos;

import lombok.Data;

/**
 * @ClassName NewsAuthDto.java
 * @Author xazhao
 * @Create 2022.08.30
 * @UpdateUser
 * @UpdateDate 2022.08.30
 * @Description
 * @Version 1.0.0
 */

@Data
public class NewsAuthDto {

    private Integer id;

    private String msg; // 信息

    private Integer page; // 当前页

    private Integer size; // 每页显示的条数

    private Integer status; // 状态

    private String title; // 标题
}
