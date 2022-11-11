package com.heima.utils.common;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ThreadLocalUtils.java
 * @Author xazhao
 * @Create 2022.08.20
 * @Description
 */

@Slf4j
public class ThreadLocalUtils {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    /**
     * 设置id
     * @Param [id]
     * @Return
     */
    public static void setUserId(Integer id) {
        threadLocal.set(id);
        log.info("设置到ThreadLocal的id为 : {}", id);
    }

    /**
     * 获取id
     * @Param []
     * @Return {@link Integer}
     */
    public static Integer getUserId() {
        log.info("获取到ThreadLocal的id为 : {}", threadLocal.get());
        return threadLocal.get();
    }

    /**
     * 删除id
     * @Param []
     * @Return
     */
    public static void deleteUserId() {
        threadLocal.remove();
    }
}
