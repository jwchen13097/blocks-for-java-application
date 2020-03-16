package org.firefly.provider.unit.test.meta;

import junit.framework.TestResult;
import junit.framework.TestSuite;

public class TestResultTest {
    public static void main(String[] args) {
        TestSuite testSuite = new TestSuite(Demo03Test.class);
        TestResult testResult = new TestResult();
        testSuite.run(testResult);
        System.out.println(testResult.errorCount());
        System.out.println(testResult.runCount());
    }
}
