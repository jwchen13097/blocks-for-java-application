package org.firefly.provider.unit.test.junit;

import org.junit.Test;

import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class AssertThatDemoTest {
    @Test
    public void testAssertThat() {
        // assertThat的基本原理是，用指定的匹配器匹配指定的实际值。如果不匹配，则生成错误信息，并抛出AssertionError异常。
        // public static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) {
        //     if (!matcher.matches(actual)) {
        //         Description description = new StringDescription();
        //         description.appendText(reason)
        //                    .appendText("\nExpected: ")
        //                    .appendDescriptionOf(matcher)
        //                    .appendText("\n     but: ");
        //         matcher.describeMismatch(actual, description);
        //
        //         throw new AssertionError(description.toString());
        //     }
        // }
        assertThat("Hello, World!", is("Hello, World!"));
        assertThat("Hello, World!", equalTo("Hello, World!"));
        assertThat("Hello, World!", startsWith("Hello"));
        assertThat("Hello, World!", endsWith("World!"));
        assertThat("Hello, World!", containsString("llo, Wor"));

        assertThat(6, not(8));

        assertThat("Hello, World!", anyOf(startsWith("Hello"), endsWith("Welcome")));
        assertThat("Hello, World!", either(startsWith("Hello")).or(endsWith("Welcome")));

        assertThat("Hello, World!", allOf(startsWith("Hello"), endsWith("World!")));
        assertThat("Hello, World!", both(startsWith("Hello")).and(endsWith("World!")));
        assertThat("Hello, World!", isA(String.class));
        assertThat("Hello, World!", anything());
        assertThat("Hello, World!", instanceOf(String.class));

        Object o = new Object();
        assertThat(o, sameInstance(o));

        assertThat(asList(1, 2, 3), hasItem(3));
        assertThat(asList(1, 2, 3), everyItem(not(8)));

        // 自定义Matcher参考assertThat("Is fails", "Hello, World!", is("Hi, World!"))的定义和失败报告。
        // 可以在一个类比org.hamcrest.CoreMatchers的工具类中提供静态方法，该方法返回自定义Matcher。
        assertThat(Set.of(1, 2, 3), new HasElement<>(2));
    }
}
