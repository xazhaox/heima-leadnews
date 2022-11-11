package com.heima.search.interceptor;

import com.heima.utils.common.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName GetUserIdInterceptor.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Component
public class GetUserIdInterceptor implements HandlerInterceptor {

    // 在请求发生之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的userId
        String userId = request.getHeader("userId");
        // 判断获取的id是否为空
        if (userId != null) {
            // 存入ThreadLocal中
            ThreadLocalUtils.setUserId(Integer.parseInt(userId));
        }
        // 放行
        return true;
    }

    // 响应之后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 删除ThreadLocal中的userId
        ThreadLocalUtils.deleteUserId();
    }
}
