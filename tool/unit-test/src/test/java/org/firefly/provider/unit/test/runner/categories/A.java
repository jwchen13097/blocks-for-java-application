package org.firefly.provider.unit.test.runner.categories;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.fail;

public class A {
    @Test
    public void a1() {
        fail();
    }

    @Category(SlowTest.class)
    @Test
    public void a2() {
    }
}
