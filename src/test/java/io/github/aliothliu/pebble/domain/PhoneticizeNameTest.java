package io.github.aliothliu.pebble.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhoneticizeNameTest {

    @Test
    public void test() {
        PhoneticizeName name = new PhoneticizeName("上善若水");
        assertEquals("ssrs", name.getPhoneticize());
    }
}