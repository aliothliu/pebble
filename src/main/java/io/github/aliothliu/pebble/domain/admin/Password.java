package io.github.aliothliu.pebble.domain.admin;

import io.github.aliothliu.pebble.PebbleRegistry;
import io.github.aliothliu.pebble.domain.ValueObject;
import io.jsonwebtoken.lang.Assert;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * Password
 *
 * @author Alioth Liu
 **/
@Embeddable
@Getter
public class Password implements ValueObject {

    private String password;

    protected Password() {
    }

    public Password(String password) {
        Assert.notNull(password, "Password should not be null");
        this.password = this.passwordService().encrypt(password);
    }

    private PasswordService passwordService() {
        return PebbleRegistry.passwordService();
    }

    public boolean matches(String plain) {
        return this.passwordService().matches(plain, this.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Password)) {
            return false;
        }
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
