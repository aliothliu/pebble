package io.github.aliothliu.pebble.domain.admin;

public interface PasswordService {

    String encrypt(String plain);

    boolean matches(String plain, String encryption);
}
