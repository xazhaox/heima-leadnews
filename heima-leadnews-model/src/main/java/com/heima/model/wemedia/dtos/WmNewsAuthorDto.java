package com.heima.model.wemedia.dtos;

import com.heima.model.wemedia.pojos.WmNews;
import lombok.Data;

/**
 * @ClassName WmNewsAuthorDto.java
 * @Author xazhao
 * @Create 2022.08.30
 * @UpdateUser
 * @UpdateDate 2022.08.30
 * @Description
 * @Version 1.0.0
 */

@Data
public class WmNewsAuthorDto extends WmNews {

    private String authorName; // 作者姓名
}
