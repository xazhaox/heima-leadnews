package com.heima.search.config;

import com.heima.search.interceptor.GetUserIdInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebMvcConfig.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Autowired
    private GetUserIdInterceptor getUserIdInterceptor;

    /**
     * 拦截路径
     * @Param [registry]
     * @Return 
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(getUserIdInterceptor).addPathPatterns("/**");
    }
}
