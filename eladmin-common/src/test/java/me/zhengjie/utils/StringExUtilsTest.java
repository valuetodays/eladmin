package me.zhengjie.utils;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static me.zhengjie.utils.StringExUtils.getWeekDay;
import static me.zhengjie.utils.StringExUtils.toCamelCase;
import static me.zhengjie.utils.StringExUtils.toCapitalizeCamelCase;
import static me.zhengjie.utils.StringExUtils.toUnderScoreCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StringExUtilsTest {

    @Test
    public void testToCamelCase() {
        assertNull(toCamelCase(null));
    }

    @Test
    public void testToCapitalizeCamelCase() {
        assertNull(StringExUtils.toCapitalizeCamelCase(null));
        assertEquals("HelloWorld", toCapitalizeCamelCase("hello_world"));
    }

    @Test
    public void testToUnderScoreCase() {
        assertNull(StringExUtils.toUnderScoreCase(null));
        assertEquals("hello_world", toUnderScoreCase("helloWorld"));
        assertEquals("\u0000\u0000", toUnderScoreCase("\u0000\u0000"));
        assertEquals("\u0000_a", toUnderScoreCase("\u0000A"));
    }

    @Test
    public void testGetWeekDay() {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
        assertEquals(simpleDateformat.format(new Date()), getWeekDay());
    }

}
