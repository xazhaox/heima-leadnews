package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.service.ApArticleConfigService;
import com.heima.model.article.pojos.ApArticleConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName ApArticleConfigServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.21
 * @Description
 */

@Slf4j
@Service
public class ApArticleConfigServiceImpl extends
        ServiceImpl<ApArticleConfigMapper, ApArticleConfig> implements ApArticleConfigService {
}
