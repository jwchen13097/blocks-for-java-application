package org.firefly.provider.unit.test.runner.parameters;

import org.firefly.provider.unit.test.domain.HelloService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.test.util.ReflectionTestUtils;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * 如果一个测试类被RunWith注解，或者继承这样的类，那么JUnit将使用该注解的值作为
 * 运行器，而非JUnit内置的运行器BlockJUnit4ClassRunner（不同的JUnit版本，默认
 * 的可能不一样，但无论哪种，都可以用@RunWith(JUnit4.class)调用默认的）。
 * <p>
 * 下面的测试叫参数化测试。
 */
@RunWith(Parameterized.class)
public class ParametersDemo01Test {
    private HelloService helloService;

    private String name;
    private String greetingWord;

    public ParametersDemo01Test(String name, String greetingWord) {
        this.name = name;
        this.greetingWord = greetingWord;
    }

    @Before
    public void setUp() {
        helloService = new HelloService();
        ReflectionTestUtils.setField(helloService, "helloPrefix", "Hello,");
    }

    @Parameters
    public static Iterable<Object[]> data() {
        return asList(new Object[][]{
                {"James", "Hello, [James]!"},
                {"Kobe", "Hello, [Kobe]!"},
                {"Joy", "Hello, [Joy]!"}
        });
    }

    @Test
    public void greetPeople() {
        assertEquals(greetingWord, helloService.greet(name));
    }
}
