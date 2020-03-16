package org.firefly.provider.unit.test.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class TestNameTest {
    @Rule
    public TestName testName = new TestName();

    @Test
    public void testTestName() {
        System.out.println(testName.getMethodName());
    }
}
