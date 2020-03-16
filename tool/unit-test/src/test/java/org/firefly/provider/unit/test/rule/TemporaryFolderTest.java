package org.firefly.provider.unit.test.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

/**
 * 该规则运行时会创建文件集和文件夹集，测试用例完成后无论成功失败都删除。
 */
public class TemporaryFolderTest {
    // 可以传入临时目录参数，否则用系统临时目录C:\Users\phewy\AppData\Local\Temp
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testTemporaryFolder() throws IOException {
        // 不存在的目录会递归创建
        // C:\Users\phewy\AppData\Local\Temp\junit15061721125969154920\11\22
        File newFolder1 = temporaryFolder.newFolder("11", "22");
        // 随机命名的目录
        // C:\Users\phewy\AppData\Local\Temp\junit15061721125969154920\junit12074361842260173715
        File newFolder2 = temporaryFolder.newFolder();
        // C:\Users\phewy\AppData\Local\Temp\junit15061721125969154920\1.txt
        File file1 = temporaryFolder.newFile("1.txt");
        // 随机命名的文件
        // C:\Users\phewy\AppData\Local\Temp\junit15061721125969154920\junit8254725417294432884.tmp
        File file2 = temporaryFolder.newFile();

        // 运行完毕后，只剩C:\Users\phewy\AppData\Local\Temp，junit15061721125969154920目录没有了。
    }
}
