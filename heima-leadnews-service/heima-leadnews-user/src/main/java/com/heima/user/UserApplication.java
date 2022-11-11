package com.heima.user;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.heima.feign.config.DefaultFeignConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.awt.*;

/**
 * @ClassName UserApplication.java
 * @Author xazhao
 * @Create 2022.08.17
 * @Description
 */

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient // 开启注册中心
@MapperScan("com.heima.user.mapper")
// 服务消费方
@EnableFeignClients(defaultConfiguration = DefaultFeignConfiguration.class,
        basePackages = {"com.heima.feign.wemedia"})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        log.info("启动成功...");
    }

    /**
     * MybatisPlus分页拦截器
     * @Param []
     * @Return {@link MybatisPlusInterceptor}
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
