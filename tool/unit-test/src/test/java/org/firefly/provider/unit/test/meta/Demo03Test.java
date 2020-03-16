package org.firefly.provider.unit.test.meta;

import org.firefly.provider.unit.test.domain.HelloService;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class Demo03Test {
    // 忽略测试，不会报告失败，但会报出忽略了多少测试用例。
    // 如果注解放到测试类上，则忽略所有测试用例。
    @Ignore("ignore because condition is not meet")
    @Test
    public void testForIgnore() {
        assertEquals(1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForException01() {
        new HelloService().greet(null);
    }

    // 测试可以比expected更深入。
    @Test
    public void testForException02() {
        try {
            new HelloService().greet(null);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (Exception e) {
            assertThat(e, is(instanceOf(IllegalArgumentException.class)));
            assertThat(e.getMessage(), is("People name is blank"));
        }
    }

    @Test(timeout = 2000)
    public void testForTimeout() throws InterruptedException {
        // 模拟耗时操作
        Thread.sleep(1000);
    }
}
