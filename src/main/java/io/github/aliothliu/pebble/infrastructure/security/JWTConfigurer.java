package io.github.aliothliu.pebble.infrastructure.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Json Web Token 适配器配置，在{@link UsernamePasswordAuthenticationFilter} 之前添加{@link JWTFilter}
 *
 * @author liubin
 * @version 1.0
 * @since 1.0
 */
public class JWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenManager tokenManager;

    public JWTConfigurer(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public void configure(HttpSecurity http) {
        JWTFilter customFilter = new JWTFilter(tokenManager);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
