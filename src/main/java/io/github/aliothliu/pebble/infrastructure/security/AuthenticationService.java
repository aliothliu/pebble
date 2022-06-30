package io.github.aliothliu.pebble.infrastructure.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Alioth Liu
 **/
@Service
public class AuthenticationService {

    private final TokenManager tokenManager;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(TokenManager tokenManager, AuthenticationManager authenticationManager) {
        this.tokenManager = tokenManager;
        this.authenticationManager = authenticationManager;
    }

    public String authenticate(String username, String password, boolean rememberMe) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenManager.create(authentication, rememberMe);
    }

    public void logout() {
        this.tokenManager.clear(((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest());
    }
}