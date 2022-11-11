package com.heima.model.wemedia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName NewsSyncDto.java
 * @Author xazhao
 * @Create 2022.08.26
 * @UpdateUser
 * @UpdateDate 2022.08.26
 * @Description
 * @Version 1.0.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsSyncDto {

    private Long articleId;

    private boolean isDown; //true 下架，false 上架

}
