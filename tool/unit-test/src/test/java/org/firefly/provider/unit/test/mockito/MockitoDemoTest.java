package org.firefly.provider.unit.test.mockito;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.stubbing.Answer;

import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

// 使用静态导入，可使代码看起来更加简洁、清爽。

/**
 * org.mockito.Mockito是Mockito框架的核心，它在继承体系中的位置如下：
 * java.lang.Object
 * org.mockito.ArgumentMatchers
 * org.mockito.Mockito
 * org.mockito.BDDMockito
 * <p>
 * Mockito的说明文档直接记录在Java文档，开发者在没有网络的情况下也能了解其使用细节。
 */
public class MockitoDemoTest {
    /**
     * 测试分为行为测试和状态测试。
     * 所谓行为测试，就是断言某个对象上发生过什么交互，即调用过什么方法及参数，以及发生过该种调用几次等。
     */
    @Test
    public void behaviourTest() {
        // 可以基于接口创建模拟对象。
        // 模拟对象一旦创建，它就会记住所有发生在它身上的交互。
        List<String> mockedList = mock(List.class);

        mockedList.add("one");

        // 没有指定已调用次数，则默认是1次，即相当于加了参数times(1)。
        verify(mockedList).add("one");
        verify(mockedList, times(1)).add("one");
        verify(mockedList, timeout(1000).times(1)).add("one");
        verify(mockedList, atLeastOnce()).add("one");
        verify(mockedList, atLeast(1)).add("one");
        verify(mockedList, atMostOnce()).add("one");
        verify(mockedList, atMost(1)).add("one");
        verify(mockedList, never()).clear();

        List<String> anotherMockedList = mock(List.class);
        // 如果调用mockedList.addAll(anotherMockedList)，则下面的断言失败。
        verifyZeroInteractions(mockedList, anotherMockedList);
        // 如果mockedList已经被调用的方法并未全部被断言，则下面的断言失败。
        // 此不宜滥用，只在逻辑上确实需要时才调用。
        verifyNoMoreInteractions(mockedList);
    }

    /**
     * 所谓状态测试，就是断言某个对象的方法被调用后返回什么值。进行了状态测试，一般就不必再进行行为测试了。
     */
    @Test
    public void stateTest() {
        // 可以基于类创建模拟对象。
        LinkedList mockedLinkedList = mock(LinkedList.class);

        // 所谓打桩（Stubbing），即规定调用模拟对象的某个方法及参数应该返回什么值。
        // 一旦打桩，将始终返回这个值，而无论调用多少次。
        when(mockedLinkedList.get(0)).thenReturn("one");
        when(mockedLinkedList.get(1)).thenThrow(new RuntimeException());

        assertThat(mockedLinkedList.get(0), is("one"));

        try {
            mockedLinkedList.get(1);
            fail("Expected a1 RuntimeException to be thrown");
        } catch (Exception e) {
            assertThat(e, is(instanceOf(RuntimeException.class)));
        }

        // 没有打桩的方法被调用后返回对应类型的默认值。
        assertNull(mockedLinkedList.get(2));

        // 针对相同方法相同参数打桩多次，那么多次调用将依次返回这些值，只是最后一次打桩将在超出的调用次数中仍然生效。
        when(mockedLinkedList.get(3)).thenReturn("four-1").thenReturn("four-2");
        assertThat(mockedLinkedList.get(3), is("four-1"));
        assertThat(mockedLinkedList.get(3), is("four-2"));
        assertThat(mockedLinkedList.get(3), is("four-2"));

        when(mockedLinkedList.get(4)).thenReturn("five-1", "five-2");
        assertThat(mockedLinkedList.get(4), is("five-1"));
        assertThat(mockedLinkedList.get(4), is("five-2"));
        assertThat(mockedLinkedList.get(4), is("five-2"));

        // 公共的打桩可以写到每个测试用例执行前执行的setUp中，每个测试用例可以重写它。但不提倡重写，因为打桩在不同地方
        // 出现，可能引起许多混淆，是一种代码坏味道。
        when(mockedLinkedList.get(5)).thenReturn("six-1");
        when(mockedLinkedList.get(5)).thenReturn("six-2");
        assertThat(mockedLinkedList.get(5), is("six-2"));
        assertThat(mockedLinkedList.get(5), is("six-2"));
        assertThat(mockedLinkedList.get(5), is("six-2"));
    }

    @Test
    public void partialMockWithSpy() {
        List<String> actualLinkedList = new LinkedList();
        // Spy对象支持部分模拟，如果调用没有被打桩的方法，那么实际方法将被调用。
        List<String> spiedLinkedList = spy(actualLinkedList);

        when(spiedLinkedList.size()).thenReturn(100);
        spiedLinkedList.add("one");
        assertThat(spiedLinkedList.get(0), is("one"));
        verify(spiedLinkedList).add("one");

        // 对Spy对象的打桩，一般采用doXXX格式，不使用thenXXX格式，因为后者有时不可用。
        doReturn("two").when(spiedLinkedList).get(1);
        assertThat(spiedLinkedList.get(1), is("two"));

        // Spy对象和被Spy的对象是两个完全独立的对象，互不影响。
        try {
            actualLinkedList.get(0);
        } catch (Exception e) {
            assertThat(e, is(instanceOf(IndexOutOfBoundsException.class)));
        }

        assertThat(spiedLinkedList.size(), is(100));
    }

    @Test
    public void partialMockWithMock() {
        LinkedList<String> mockedLinkedList = mock(LinkedList.class);

        when(mockedLinkedList.add("one")).thenCallRealMethod();
        when(mockedLinkedList.get(0)).thenCallRealMethod();

        mockedLinkedList.add("one");
        assertThat(mockedLinkedList.get(0), is("one"));
        assertNull(mockedLinkedList.get(1));
    }

    /**
     * Mockito对参数值匹配的判断，默认使用的是Java的equals方法。
     */
    @Test
    public void argumentMatcher() {
        LinkedList<String> mockedLinkedList = mock(LinkedList.class);

        // 使用内置的参数匹配器打桩。
        when(mockedLinkedList.get(eq(999))).thenReturn("element");
        assertThat(mockedLinkedList.get(999), is("element"));
        verify(mockedLinkedList).get(anyInt());

        // 使用自定义的参数匹配器打桩。参数匹配一般不使用自定义的，而是用equals或内置的参数匹配器。
        when(mockedLinkedList.addAll(argThat(strings -> strings.size() == 2))).thenReturn(true);
        assertTrue(mockedLinkedList.addAll(asList("one", "two")));
        verify(mockedLinkedList).addAll(argThat(strings -> strings.size() == 2));
    }

    @Test
    public void argumentCaptor() {
        LinkedList<String> mockedLinkedList = mock(LinkedList.class);

        mockedLinkedList.add("one");
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockedLinkedList).add(argumentCaptor.capture());
        assertEquals("one", argumentCaptor.getValue());
    }

    @Test
    public void resetMock() {
        LinkedList<String> mockedLinkedList = mock(LinkedList.class);
        when(mockedLinkedList.size()).thenReturn(10);
        mockedLinkedList.add("one");

        // 忘记所有打桩和交互。
        reset(mockedLinkedList);
        assertThat(mockedLinkedList.size(), is(0));
        verify(mockedLinkedList, never()).add("one");
    }

    @Test
    public void verifyInvokeOrder() {
        List<String> mockedList = mock(List.class);
        mockedList.add("one");
        mockedList.add("two");
        // 验证mockedList.add("one")调用先于mockedList.add("two")。
        InOrder inOrder = inOrder(mockedList);
        inOrder.verify(mockedList).add("one");
        inOrder.verify(mockedList).add("two");

        List<String> firstMockedList = mock(List.class);
        List<String> secondMockedList = mock(List.class);
        List<String> thirdMockedList = mock(List.class);
        firstMockedList.add("one");
        secondMockedList.add("two");
        thirdMockedList.add("three");
        // 只需要选择感兴趣的调用，验证这些调用按指定顺序就行。
        InOrder anotherInOrder = inOrder(firstMockedList, secondMockedList);
        anotherInOrder.verify(firstMockedList).add("one");
        anotherInOrder.verify(secondMockedList).add("two");
    }

    /**
     * 定制具体的返回，即可以根据不同的场景返回不同的值。
     */
    @Test
    public void thenAnswer() {
        LinkedList<String> mockedLinkedList = mock(LinkedList.class);

        when(mockedLinkedList.add(anyString())).thenAnswer(
                (Answer<Boolean>) invocation -> {
                    Object[] arguments = invocation.getArguments();
                    return arguments.length == 1 && arguments[0].equals("one");
                }
        );

        assertTrue(mockedLinkedList.add("one"));
    }

    @Test
    public void doMethods() {
        LinkedList<String> mockedLinkedList = mock(LinkedList.class);

        doReturn(true).when(mockedLinkedList).add("one");
        assertTrue(mockedLinkedList.add("one"));

        doThrow(new RuntimeException()).when(mockedLinkedList).add("one");
        try {
            mockedLinkedList.add("one");
        } catch (Exception e) {
            assertThat(e, is(instanceOf(RuntimeException.class)));
        }

        doAnswer(
                (Answer<Boolean>) invocation -> {
                    Object[] arguments = invocation.getArguments();
                    return arguments.length == 1 && arguments[0].equals("one");
                }).when(mockedLinkedList).add("one");
        assertTrue(mockedLinkedList.add("one"));

        doNothing().when(mockedLinkedList).clear();
        mockedLinkedList.clear();

        doCallRealMethod().when(mockedLinkedList).add("one");
        doCallRealMethod().when(mockedLinkedList).get(0);
        mockedLinkedList.add("one");
        assertThat(mockedLinkedList.get(0), is("one"));
    }
}
