package com.heima.model.user.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName LoginDto.java
 * @Author xazhao
 * @Create 2022.08.17
 * @Description
 */

@Data
public class LoginDto {

    @ApiModelProperty(value = "手机号", required = true)
    private String phone; // 手机号

    @ApiModelProperty(value = "密码", required = true)
    private String password; // 密码
}
