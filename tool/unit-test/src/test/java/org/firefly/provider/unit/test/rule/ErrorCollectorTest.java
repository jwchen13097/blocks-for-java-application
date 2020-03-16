package org.firefly.provider.unit.test.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

/**
 * 不让一出现异常就失败，而是可以收集多个失败的情况。
 */
public class ErrorCollectorTest {
    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Test
    public void testErrorController() {
//        errorCollector.addError(new Throwable("start error"));
//        errorCollector.addError(new Throwable("second error"));
    }
}
