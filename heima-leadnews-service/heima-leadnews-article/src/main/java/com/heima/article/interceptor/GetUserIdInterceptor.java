package com.heima.article.interceptor;

import com.heima.utils.common.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName GetUserIdInterceptor.java
 * @Author xazhao
 * @Create 2022.08.20
 * @Description
 */

@Slf4j
@Component
public class GetUserIdInterceptor implements HandlerInterceptor {

    // 在请求发生之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中用户id, 存入ThreadLocal中
        String userId = request.getHeader("userId");
        log.info("获取到请求头中的id为 : {}", userId);
        // 判断用户的id是否为空
        if (userId != null) {
            // 存入ThreadLocal中
            ThreadLocalUtils.setUserId(Integer.parseInt(userId));
        }
        log.info("存入ThreadLocal中的id为 : {}", ThreadLocalUtils.getUserId());
        // 放行
        return true;
    }

    // 响应之后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("删除的ThreadLocal的id为 : {}", ThreadLocalUtils.getUserId());
        // 请求结束之后删除ThreadLocal中的用户id
        ThreadLocalUtils.deleteUserId();
    }
}
