package io.github.aliothliu.pebble.infrastructure.security;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author Alioth Liu
 **/
public interface TokenManager {

    /**
     * 获取新的token
     *
     * @param authentication 认证对象
     * @param rememberMe     是否记住我
     * @return token字符串
     */
    String create(Authentication authentication, boolean rememberMe);

    /**
     * 验证token是否合法
     *
     * @param token token
     * @return true表示正确token，false反之
     */
    boolean valid(String token);

    /**
     * 从request中获取token
     *
     * @param request 请求对象
     * @return token字符串
     */
    Optional<String> readFrom(HttpServletRequest request);

    /**
     * 计算是否需要刷新token
     *
     * @param token token
     * @return true 表示需要刷新,false 反之
     */
    boolean refreshable(String token);

    /**
     * 获取 Account 对象
     *
     * @param token token
     * @return Account 对象
     */
    Authentication rebuild(String token);

    /**
     * 清除请求token
     *
     * @param request 请求对象
     */
    void clear(HttpServletRequest request);
}
