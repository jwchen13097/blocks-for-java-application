package org.firefly.provider.unit.test.runner.console;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Ctrl+Shift+F10，运行的则是IntelliJ IDEA内置的测试运行器。
 */
public class DemoTest {
    @Ignore
    @Test
    public void testEquals() {
        assertEquals("Hello, World!", "Hi, World!");
    }
}
