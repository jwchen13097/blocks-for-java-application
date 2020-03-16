package org.firefly.provider.unit.test.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertTrue;

public class RuleChainTest {
    @Rule
    public final TestRule chain = RuleChain
            .outerRule(new CustomerTestRule("A"))
            .around(new CustomerTestRule("B"))
            .around(new CustomerTestRule("C"));

    @Test
    public void testRuleChain() {
        assertTrue(true);
    }
}
