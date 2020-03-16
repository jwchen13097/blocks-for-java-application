package org.firefly.provider.unit.test.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.IOException;

/**
 * 运行测试之前准备好外部资源，如文件、socket、服务器启动、数据库连接等，并保证运行完毕后释放资源。
 */
public class ExternalResourceTest {
    private File temporaryFile;

    @Rule
    public ExternalResource externalResource = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            temporaryFile = File.createTempFile("1111", ".txt");
        }

        @Override
        protected void after() {
            temporaryFile.delete();
        }
    };

    @Test
    public void testExternalResource() throws IOException {
        System.out.println(temporaryFile.getCanonicalPath());
    }
}
