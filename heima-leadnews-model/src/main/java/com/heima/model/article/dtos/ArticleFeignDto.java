package com.heima.model.article.dtos;

import com.heima.model.wemedia.pojos.WmNews;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ArticleFeignDto.java
 * @Author xazhao
 * @Create 2022.08.21
 * @Description
 */

@Data
public class ArticleFeignDto extends WmNews {

    private String channelName; // 频道名称

    private String authorName; // 作者昵称
}
