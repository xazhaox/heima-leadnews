package com.heima.utils.common;

import com.alibaba.fastjson.JSON;
import com.heima.model.wemedia.dtos.Content;

import java.util.List;

/**
 * @ClassName DataAnalysisUtils.java
 * @Author xazhao
 * @Create 2022.08.28
 * @UpdateUser
 * @UpdateDate 2022.08.28
 * @Description
 * @Version 1.0.0
 */
public class DataAnalysisUtils {

    /**
     * 解析文章中的文本部分
     * @Param [content]
     * @Return {@link String}
     */
    public static String analysisOfArticleContent(String content) {
        StringBuilder contentText = new StringBuilder();
        // 将字符传转为集合
        List<Content> contentsList = JSON.parseArray(content, Content.class);
        for (Content contents : contentsList) {
            // 判断type为text的
            if (contents.getType().equals("text")) {
                // 将type为text的值存入contentText
                contentText.append(contents.getValue());
            }
        }
        // 返回内容中文本部分
        return contentText.toString();
    }
}
