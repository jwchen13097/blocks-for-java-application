package org.firefly.provider.unit.test.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.concurrent.TimeUnit;

public class TimeoutTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    @Test
    public void testForTimeout() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
    }
}
