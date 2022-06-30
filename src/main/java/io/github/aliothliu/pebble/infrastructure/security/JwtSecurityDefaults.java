package io.github.aliothliu.pebble.infrastructure.security;

public final class JwtSecurityDefaults {

    interface Authentication {

        interface Jwt {

            String secret = null;
            String base64Secret = null;
            long tokenValidityInSeconds = 1800; // 半小时
            long tokenValidityInSecondsForRememberMe = 2592000; // 30 小时;
        }
    }
}
