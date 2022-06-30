package io.github.aliothliu.pebble.infrastructure.security.token;

import io.github.aliothliu.pebble.infrastructure.security.JwtSecurityProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author Alioth Liu
 **/
public final class DefaultTokenManager extends AbstractTokenManager {

    public DefaultTokenManager(JwtSecurityProperties securityProperties) {
        super(securityProperties);
    }

    @Override
    protected boolean canCreate() {
        return true;
    }

    @Override
    protected String reusableToken() {
        throw new UnsupportedOperationException("Not support reusable token");
    }

    @Override
    protected void persistToken(String token, Date createDate, Date validity) {
        // do nothing
    }

    @Override
    protected boolean isAvailable(String token) {
        return true;
    }

    @Override
    public void clear(HttpServletRequest request) {
        // do nothing
    }
}
