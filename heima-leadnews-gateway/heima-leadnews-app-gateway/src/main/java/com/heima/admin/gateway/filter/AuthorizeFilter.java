package com.heima.admin.gateway.filter;

import com.heima.admin.gateway.util.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器实现JWT校验
 * @ClassName AuthorizeFilter.java
 * @Author xazhao
 * @Create 2022.08.17
 * @Description
 */

@Slf4j
@Component
public class AuthorizeFilter implements Ordered, GlobalFilter {

    /**
     * Process the Web request and (optionally) delegate to the next {@code WebFilter}
     * through the given {@link GatewayFilterChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     *
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取request, response
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 获取路径中/login
        boolean contains = request.getURI().getPath().contains("/login");
        // 判断路径中是否有/login
        if (contains) {
            // true 在登录页面, 放行
            log.info("当前在登录页面, 放行.");
            return chain.filter(exchange);
        }

        // 判断Header头中是否有token
        String token = request.getHeaders().getFirst("token");
        // 判断token是否为空或空字符串
        if (StringUtils.isBlank(token)) {
            // 为null, 状态设置为401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            log.info("token为空, 返回401.");
            // 返回401
            return response.setComplete();
        }

        // token存在, 判断token是否有效
        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            // 获取token的负载部分
            Object userId = claimsBody.get("id");
            // 设置到请求头中
            request.mutate().header("userId", String.valueOf(userId));
            // 获取token状态
            int verifyToken = AppJwtUtil.verifyToken(claimsBody);
            // 判断token是否过期
            if (verifyToken == 1 || verifyToken == 2) {
                // 过期, 状态设置为401
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                log.info("token过期, 返回401.");
                // 返回401
                return response.setComplete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常, 状态设置为401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            log.info("出现异常, 返回401.");
            // 返回401
            return response.setComplete();
        }

        log.info("登录成功, 放行.");
        // 登录成功, 放行
        return chain.filter(exchange);
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     * 优先级设置, 值越小过滤器执行的优先级越高
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
