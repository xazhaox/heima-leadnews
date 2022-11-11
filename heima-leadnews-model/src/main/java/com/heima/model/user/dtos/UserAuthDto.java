package com.heima.model.user.dtos;

import lombok.Data;

/**
 * @ClassName UserAuthDto.java
 * @Author xazhao
 * @Create 2022.08.31
 * @UpdateUser
 * @UpdateDate 2022.08.31
 * @Description
 * @Version 1.0.0
 */

@Data
public class UserAuthDto {

    private Integer id;

    private String msg; // 信息

    private Integer page; // 当前页

    private Integer size; // 每页显示的条数

    private Integer status; // 状态
}
