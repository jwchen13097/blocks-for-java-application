package org.firefly.provider.unit.test.junit;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Set;

/**
 * 自定义Matcher必须实现Matcher，其泛型类型Set<? super T>对应待判断的实际值的类型。
 */
public class HasElement<T> implements Matcher<Set<? super T>> {
    private final T element;

    public HasElement(T element) {
        this.element = element;
    }

    // 判断逻辑
    @Override
    public boolean matches(Object actualSet) {
        return ((Set) actualSet).contains(element);
    }

    // 失败时的实际的描述
    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        mismatchDescription.appendText("The set was ").appendValue(item);
    }

    @Override
    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

    }

    // 失败时的期望的描述
    @Override
    public void describeTo(Description description) {
        description.appendText("The given set contains " + element);
    }
}
