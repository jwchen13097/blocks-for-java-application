package org.firefly.provider.unit.test.rule;

import org.junit.Rule;
import org.junit.Test;

public class CustomerTestRuleTest {
    @Rule
    public final CustomerTestRule logger = new CustomerTestRule("Customer");

    @Test
    public void testCustomerTestRule() {
    }
}