package org.firefly.provider.unit.test.meta;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class TestCaseDemoTest extends TestCase {
    private double fValue1;
    private double fValue2;

    @Before
    public void setUp() {
        fValue1 = 2.0;
        fValue2 = 3.0;
    }

    @Test
    public void testAdd() {
        System.out.println("Plus two numbers = " + (fValue1 + fValue2));

        System.out.println("Number of test cases = " + this.countTestCases());

        System.out.println("Test case came = " + this.getName());

        this.setName("testNewAdd");
        System.out.println("Updated test case name = " + this.getName());
    }
}