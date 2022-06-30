package io.github.aliothliu.pebble.domain;

import io.github.aliothliu.marble.domain.AssertionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellphoneTest {
    @Test
    void npe() {
        Exception exception = assertThrows(AssertionException.class, () -> new Cellphone(null));

        assertTrue(exception.getMessage().contains("移动手机号不能为空"));
    }

    @Test
    void errorFormat() {
        Exception exception = assertThrows(AssertionException.class, () -> new Cellphone("12345"));

        assertTrue(exception.getMessage().contains("移动手机号[12345]格式错误"));
    }

    @Test
    void test() {
        assertEquals("14788085446", new Cellphone("14788085446").getNumber());
    }
}