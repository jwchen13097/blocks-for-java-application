package org.firefly.provider.unit.test.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// 类名上按Alt+Ins并选择Test...，进行简单的配置就可以创建单元测试。
@Service
public class HelloService {
    @Value("${hello.prefix}")
    private String helloPrefix;

    public String greet(String name) {
        if (name == null || "".equals(name.trim())) {
            throw new IllegalArgumentException("People name is blank");
        }

        return helloPrefix + " " + wrapperName(name) + "!";
    }

    public String wrapperName(String name) {
        return "[" + name + "]";
    }
}
