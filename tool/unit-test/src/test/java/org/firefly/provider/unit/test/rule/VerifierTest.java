package org.firefly.provider.unit.test.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Verifier;

/**
 * 等价于测试用例执行完毕后，使用verify的方法体内置测试用例末尾运行。
 */
public class VerifierTest {
    private String result;

    @Rule
    public Verifier verifier = new Verifier() {
        @Override
        protected void verify() throws Throwable {
            if (!"Success".equals(result)) {
                throw new Exception("test fail");
            }
        }
    };

    @Test
    public void testVerify() {
//        result = "Fail";
        result = "Success";
    }
}
