package org.firefly.provider.unit.test.rule;

import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.logging.Logger;

/**
 * 自定义Rule是建立在知道运行器原理的基础上的。见{@link RunRules}。
 */
public class CustomerTestRule implements TestRule {
    private Logger logger;
    private String name;

    public CustomerTestRule(String name) {
        this.name = name;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                logger = Logger.getLogger(description.getTestClass().getName() + '.' + description.getDisplayName());
                logger.warning("Your test is showing! Rule name: " + name);
                base.evaluate();
            }
        };
    }
}