package io.github.aliothliu.pebble.infrastructure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWTFilter 过滤Http请求，并判断是否附带Json Web Token 头部数据
 * <p>
 * 如果token合法，则会在SecurityContextHolder中设置当前用户为已认证，否则则交由其他Filter处理
 * </p>
 *
 * @author liubin
 * @version 1.0
 * @since 1.0
 */
public class JWTFilter extends GenericFilterBean {

    private static final String PREFIX_JWT = "Bearer ";
    private final TokenManager tokenManager;

    public JWTFilter(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = this.tokenManager.readFrom(httpServletRequest).orElse(null);
        if (StringUtils.hasText(jwt) && this.tokenManager.valid(jwt)) {
            Authentication authentication = this.tokenManager.rebuild(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if (this.tokenManager.refreshable(jwt)) {
                this.writeTokenToResponse(jwt, (HttpServletResponse) servletResponse);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void writeTokenToResponse(String token, HttpServletResponse servletResponse) {
        servletResponse.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, PREFIX_JWT + tokenManager.create(tokenManager.rebuild(token), Boolean.FALSE));
    }
}
