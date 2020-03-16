package org.firefly.provider.unit.test.runner.console;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class ConsoleDemoTest {
    public static void main(String[] args) {
        // 基于Console的测试运行器。
        // 等价于：java org.junit.runner.JUnitCore TestClass1 [...other test classes...]
        // 原理见：https://www.jianshu.com/p/ad524e211ef3
        // JUnitCore是运行测试的外观，创建它的实例来运行测试时，还可以添加监听器。
        // 运行的测试类，也可以是套件测试。
        Result result = JUnitCore.runClasses(DemoTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
