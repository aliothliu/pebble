package io.github.aliothliu.pebble.infrastructure.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdCardUtilTest {

    @Test
    void isValidCard() {
        String idCard = "110101199003072674";
        assertTrue(IdCardUtil.isValidCard(idCard));

        assertEquals(1, IdCardUtil.getGenderByIdCard(idCard));
        assertEquals((short) 1990, IdCardUtil.getYearByIdCard(idCard));
    }
}