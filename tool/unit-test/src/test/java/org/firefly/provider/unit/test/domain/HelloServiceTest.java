package org.firefly.provider.unit.test.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;

/**
 * JUnit是一个针对Java语言的单元测试的框架，由Kent Beck和Erich Gamma建立。
 * 由JUnit开始目前已衍生出了一系列xUnit。
 * 单元测试是软件高质量的一个指标。
 * <p>
 * TDD，测试驱动开发，即编写产品代码前，先写单元测试。这样做的好处是：
 * 1）让单元测试成为软件设计的一部分。反向看，如果先写产品代码，可能写出高耦合的代码，那么我们会感到写测试比较难。反之，先写测
 * 试再写产品代码，那么产品代码天然就避开了强耦合。
 * 2）真正的质量保证。如果先写代码，有的开发者为了完成单元测试覆盖率指标，就可能会先运行程序，再将结果写到单元测试中，这就是那
 * 些烦单元测试的人所说的“单元测试是自欺欺人”。而实际上，正确的步骤应该是先写测试代码，就保证了所写功能的验收标准，就不能“自
 * 欺欺人”了。实际上，单元测试和产品代码可由两个不同的人写，一个人先写单元测试，另一个人负责写产品代码让单元测试通过。
 * <p>
 * 一个测试用例的测试边界在哪？应该是这样的，测试覆盖它所有的逻辑，但依赖对象的行为则不是这个用例该做的，应该是依赖对象类的测
 * 试用例来保证。
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HelloServiceTest {
    @InjectMocks
    @Spy
    private HelloService helloService;

    // fixture annotations包括@BeforeClass、@AfterClass、@Before、@After，test fixture是为了保证测试用例
    // 运行前有个固定的环境，让测试可重现。
    @BeforeClass
    public static void start() {
        System.out.println(BeforeClass.class.getSimpleName());
    }

    @AfterClass
    public static void stop() {
        System.out.println(AfterClass.class.getSimpleName());
    }

    @Before
    public void setUp() {
        System.out.println(Before.class.getSimpleName());
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(helloService, "helloPrefix", "Hello,");
    }

    @After
    public void tearDown() {
        System.out.println(After.class.getSimpleName());
        helloService = null;
    }

    // 空白处按Att+Enter并选择Test Method，则创建一个测试用例。

    @Test
    public void shouldSayHelloWhenGreetPeople() {
        String name = "cjw";
        // 因为使用了@Spy注解，所以可以为helloService的方法打桩。
        doReturn("\"" + name + "\"").when(helloService).wrapperName(name);

        String greetingWord = helloService.greet(name);

        assertThat(greetingWord, is("Hello, \"cjw\"!"));
    }

    // 测试用例一般采用三段式描述结构：准备数据，执行动作，判断结果。
    @Test
    public void shouldReturnModifiedNameWhenWrapperName() {
        // 准备数据：被调用方法的参数等的准备
        String name = "cjw";

        // 执行动作
        String wrapperName = helloService.wrapperName(name);

        // 判断结果
        assertThat(wrapperName, is("[cjw]"));
    }

    @Test
    public void shouldThrowExceptionWhenNameIsBlank() {
        String name = "";

        try {
            helloService.greet(name);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (Exception e) {
            assertThat(e, is(instanceOf(IllegalArgumentException.class)));
            assertThat(e.getMessage(), is("People name is blank"));
        }
    }
}