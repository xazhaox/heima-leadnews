package com.heima.behavior.config;

import com.heima.behavior.interceptor.GetUserIdInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebMvcConfig.java
 * @Author xazhao
 * @Create 2022.08.20
 * @Description
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private GetUserIdInterceptor getUserIdInterceptor;

    /**
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //
        registry.addInterceptor(getUserIdInterceptor).addPathPatterns("/**");
    }
}
