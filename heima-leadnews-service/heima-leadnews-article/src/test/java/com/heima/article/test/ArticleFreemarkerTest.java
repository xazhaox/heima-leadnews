package com.heima.article.test;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonArray;
import com.heima.article.ArticleApplication;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleContentService;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import freemarker.template.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ArticleFreemarkerTest.java
 * @Author xazhao
 * @Create 2022.08.19
 * @Description
 */

@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class ArticleFreemarkerTest {

    @Autowired
    private Configuration configuration;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleContentService apArticleContentService;

    @Test
    public void createStaticUrlTest() throws IOException, TemplateException {
        // 查询所有
        List<ApArticleContent> contentList = apArticleContentService.list();
        // 遍历查询出的数据
        for (ApArticleContent content : contentList) {
            // 判断content不为空并且不为空字符串
            if (content != null && StringUtils.isNoneBlank(content.getContent())) {
                // 文章内容通过freemarker生成html文件
                Template template = configuration.getTemplate("article.ftl");
                Map<String, Object> params = new HashMap<>();
                params.put("content", JSONArray.parseArray(content.getContent()));

                StringWriter stringWriter = new StringWriter();
                template.process(params, stringWriter);

                InputStream inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes());

                // 把html文件上传到minio中
                String path = fileStorageService.uploadHtmlFile("",
                        content.getArticleId() + ".html", inputStream);

                // 修改ap_article表, 保存static_url字段
                ApArticle apArticle = new ApArticle();
                apArticle.setId(content.getArticleId());
                apArticle.setStaticUrl(path);
                apArticleMapper.updateById(apArticle);
            }
        }
    }
}
