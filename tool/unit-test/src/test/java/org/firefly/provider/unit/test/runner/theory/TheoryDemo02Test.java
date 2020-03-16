package org.firefly.provider.unit.test.runner.theory;

import org.junit.Assume;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

// 理论测试
@RunWith(Theories.class)
public class TheoryDemo02Test {
    @Theory
    public final void shouldResultNotLessThanZeroWhenAddonNotLessThanZero(
            @NumberRange(end = 0) int i, @NumberRange(start = -3, end = 10) int j) {
        Assume.assumeTrue(i >= 0 && j >= 0);

        assertTrue(i + j >= 0);
    }
}
