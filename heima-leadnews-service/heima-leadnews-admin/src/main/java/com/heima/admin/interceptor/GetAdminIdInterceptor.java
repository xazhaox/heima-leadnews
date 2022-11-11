package com.heima.admin.interceptor;

import com.heima.utils.common.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName GetAdminIdInterceptor.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Component
public class GetAdminIdInterceptor implements HandlerInterceptor {

    /**
     * 在请求发生之前执行
     * @Param [request, response, handler]
     * @Return {@link boolean}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取id
        String adminId = request.getHeader("AdminId");
        log.info("adminId {}", adminId);
        // 判断userid是否为空
        if (adminId != null) {
            // 存入ThreadLocal中
            ThreadLocalUtils.setUserId(Integer.parseInt(adminId));
            log.info("ThreadLocalUtils {}", ThreadLocalUtils.getUserId());
        }
        // 放行
        return true;
    }

    /**
     * 响应之后执行
     * @Param [request, response, handler, modelAndView]
     * @Return
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 删除ThreadLocal中AdminId
        ThreadLocalUtils.deleteUserId();
    }
}
