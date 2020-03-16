package org.firefly.provider.unit.test.junit;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * JUnit为所有原生类型、Objects以及它们的数组提供了重载的断言方法。定义这许多重载的断言方法，特别是assertThat，目的只有一
 * 个：增强断言的表达力，得到更好的失败报告。assertTrue(msg != null && msg.startsWith("Hello") && msg.endsWith("World"))，
 * 使用assertThat(msg, both(startsWith("Hello")).and(endsWith("World")))则更有表达力。assertEquals是谓宾主结构，而
 * assertThat是主谓宾结构，更自然，也可以组合多个Matcher。
 */
public class AssertCommonDemoTest {
    /**
     * 这些断言都可以在第一个参数前插入失败时的提示消息，没有插入的相当于插入了null。
     */
    @Test
    public void commonAssertions() {
        // 这是两个Object，第一个是期望值，第二个是实际值。
        // 1）如果都为null或equals判等通过则通过。
        // 2）如果不过，且都为String，则抛new ComparisonFailure(cleanMessage, (String) expected, (String) actual)，
        // message为null则cleanMessage为空字符串，AssertionError的子类。
        // 3）其他不过的情况，也给出实际值和期望值相应的明确说明，封装为异常AssertionError。
        assertEquals("test", "test");
        assertEquals(34.3, 35.6, 2);
        assertNotEquals("test01", "test02");
        assertArrayEquals(new int[]{1, 2, 3}, new int[]{1, 2, 3});

        // 为true则通过，否则执行fail
        assertTrue(true);
        // assertTrue基础上对condition取反得到
        assertFalse(false);

        assertNull(null);
        assertNotNull(new Object());

        Object o1 = new Object();
        Object o2 = o1;
        assertSame(o1, o2);
        assertNotSame(new Object(), new Object());
    }

    @Test(expected = AssertionError.class)
    public void testFail() {
        // 没有参数则直接抛new AssertionError()，否则抛new AssertionError(message)。AssertionError继承自Error。
        fail();
    }
}
