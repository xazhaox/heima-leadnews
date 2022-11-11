package com.heima.model.wemedia.dtos;

import lombok.Data;

/**
 * @ClassName WmSensitiveDto.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

@Data
public class WmSensitiveDto {

    private String name; // 敏感词

    private Integer page; // 当前页

    private Integer size; // 每页显示的条数
}
