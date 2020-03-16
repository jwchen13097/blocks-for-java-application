package org.firefly.provider.unit.test.rule;

import org.firefly.provider.unit.test.domain.HelloService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.containsString;

public class ExpectedExceptionTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testForException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("People name is blank");
        expectedException.expectMessage(containsString("name is blank"));
        new HelloService().greet(null);
    }
}
