package org.firefly.provider.file.watchdog;

import java.awt.Frame;
import java.awt.Toolkit;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Date;
import java.util.concurrent.Executors;

public class FileWatchdog {
    private WatchService watchService;

    public FileWatchdog() throws IOException {
        watchService = FileSystems.getDefault().newWatchService();
    }

    public void addDirectory(String directory) throws IOException {
        Paths.get(directory).register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
    }

    public void start() {
        Executors.newSingleThreadExecutor().execute(() -> {
            while (true) {
                WatchKey watchKey = null;
                try {
                    watchKey = watchService.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (WatchEvent watchEvent : watchKey.pollEvents()) {
                    Frame frame = new Frame();
                    Toolkit toolkit = frame.getToolkit();
                    toolkit.beep();

                    System.out.println();
                    System.out.println(new Date());
                    System.out.println("Context path: " + ((Path) watchEvent.context()).toFile().getAbsolutePath());
                    System.out.println("Watch event kind: " + watchEvent.kind());
                }

                watchKey.reset();
            }
        });
    }
}
