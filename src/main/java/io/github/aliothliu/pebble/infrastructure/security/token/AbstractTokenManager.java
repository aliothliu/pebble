package io.github.aliothliu.pebble.infrastructure.security.token;

import io.github.aliothliu.pebble.infrastructure.security.JWTConfigurer;
import io.github.aliothliu.pebble.infrastructure.security.JwtSecurityProperties;
import io.github.aliothliu.pebble.infrastructure.security.TokenManager;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Alioth Liu
 **/
public abstract class AbstractTokenManager implements TokenManager {

    private static final String PREFIX_JWT = "Bearer ";
    private static final String AUTHORITIES_KEY = "auth";

    private final Logger log = LoggerFactory.getLogger(AbstractTokenManager.class);

    private final JwtSecurityProperties securityProperties;

    private String secretKey;

    private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;

    public AbstractTokenManager(JwtSecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @PostConstruct
    public void init() {
        this.secretKey =
                securityProperties.getAuthentication().getJwt().getSecret();

        this.tokenValidityInMilliseconds =
                1000 * securityProperties.getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
                1000 * securityProperties.getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();
    }

    @Override
    public String create(Authentication authentication, boolean rememberMe) {
        if (!this.canCreate()) {
            return this.reusableToken();
        }
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        String newToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setExpiration(validity)
                .compact();
        this.persistToken(newToken, new Date(), validity);
        return newToken;
    }

    protected abstract boolean canCreate();

    protected abstract String reusableToken();

    protected abstract void persistToken(String token, Date createDate, Date validity);

    @Override
    public boolean valid(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return this.isAvailable(token);
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e.getMessage());
        }
        return false;
    }

    protected abstract boolean isAvailable(String token);

    @Override
    public Optional<String> readFrom(HttpServletRequest request) {
        String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX_JWT)) {
            return Optional.of(bearerToken.substring(PREFIX_JWT.length()));
        }
        return Optional.empty();
    }

    @Override
    public boolean refreshable(String token) {
        long now = System.currentTimeMillis();
        Claims claims = Jwts.parser().setSigningKey(securityProperties.getAuthentication().getJwt().getSecret()).parseClaimsJws(token).getBody();
        long exp = claims.getExpiration().getTime();
        return exp - now < securityProperties.getAuthentication().getJwt().getTokenValidityInSeconds() * 200;
    }

    @Override
    public Authentication rebuild(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .filter(StringUtils::hasText)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), token, authorities);
    }
}
