package com.heima.model.article.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("ap_article_content")
public class ApArticleContent implements Serializable {

    @TableId(value = "id",type = IdType.ID_WORKER)
    @JsonFormat(shape = JsonFormat.Shape.STRING) // 将传给前端的id转为string, 防止精度丢失
    private Long id;

    /**
     * 文章id
     */
    @TableField("article_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING) // 将传给前端的id转为string, 防止精度丢失
    private Long articleId;

    /**
     * 文章内容
     */
    private String content;
}
