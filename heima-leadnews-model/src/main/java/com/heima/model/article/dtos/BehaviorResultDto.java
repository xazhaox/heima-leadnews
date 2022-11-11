package com.heima.model.article.dtos;

import lombok.Data;

/**
 * @ClassName BehaviorResultDto.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */

@Data
public class BehaviorResultDto {

    private boolean islike;

    private boolean isunlike;

    private boolean iscollection;

    private boolean isfollow;
}
