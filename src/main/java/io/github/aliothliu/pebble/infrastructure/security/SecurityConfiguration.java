package io.github.aliothliu.pebble.infrastructure.security;

import io.github.aliothliu.pebble.domain.admin.PasswordService;
import io.github.aliothliu.pebble.infrastructure.security.token.DefaultTokenManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

/**
 * @author Alioth Liu
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

    private final SecurityProblemSupport problemSupport;

    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(SecurityProblemSupport problemSupport,
                                 UserDetailsService userDetailsService) {
        super();
        this.problemSupport = problemSupport;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/ops/authenticate").permitAll()
                .antMatchers("/**/*.{js,html}").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/api-docs").permitAll()
                .antMatchers("/doc.html").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .apply(this.securityConfigurerAdapter())
                .and()
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        ProviderManager authenticationManager = new ProviderManager(this.authenticationProvider());
        // 不擦除认证密码，擦除会导致TokenBasedRememberMeServices因为找不到Credentials再调用UserDetailsService而抛出UsernameNotFoundException
        authenticationManager.setEraseCredentialsAfterAuthentication(false);
        return authenticationManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordService passwordService(final PasswordEncoder passwordEncoder) {

        return new PasswordService() {
            @Override
            public String encrypt(String plain) {
                return passwordEncoder.encode(plain);
            }

            @Override
            public boolean matches(String plain, String encryption) {
                return passwordEncoder.matches(plain, encryption);
            }
        };
    }

    @Bean
    @ConfigurationProperties(prefix = "pebble.security")
    public JwtSecurityProperties securityProperties() {
        return new JwtSecurityProperties();
    }

    @Bean
    public TokenManager tokenManager() {
        return new DefaultTokenManager(this.securityProperties());
    }

    protected JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(this.tokenManager());
    }
}
