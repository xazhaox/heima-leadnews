package com.heima.model.admin.dtos;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @ClassName AdUserDto.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

@Data
public class AdUserDto {

    private String name; // 登录用户名

    private String password; // 登录密码
}
