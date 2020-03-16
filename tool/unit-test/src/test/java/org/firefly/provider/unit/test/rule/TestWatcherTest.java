package org.firefly.provider.unit.test.rule;

import org.junit.Assume;
import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class TestWatcherTest {
    @Rule
    public final TestRule watchman = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            System.out.println(description.getDisplayName() + " Succeed");
        }

        @Override
        protected void failed(Throwable e, Description description) {
            System.out.println(description.getDisplayName() + " Failed");
        }

        @Override
        protected void skipped(AssumptionViolatedException e, Description description) {
            System.out.println(description.getDisplayName() + " Skipped");
        }

        @Override
        protected void starting(Description description) {
            System.out.println(description.getDisplayName() + " Started");
        }

        @Override
        protected void finished(Description description) {
            System.out.println(description.getDisplayName() + " Finished");
        }
    };

    @Test
    public void fails() {
//        fail();
    }

    @Test
    public void succeeds() {
    }

    @Test
    public void skips() {
        Assume.assumeTrue(false);
    }
}
