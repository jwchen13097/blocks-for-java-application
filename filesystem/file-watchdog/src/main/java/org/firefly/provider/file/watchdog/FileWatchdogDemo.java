package org.firefly.provider.file.watchdog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class FileWatchdogDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Number of arguments is limited to be one.");
        }

        FileWatchdog fileWatchdog = new FileWatchdog();
        fileWatchdog.addDirectory(args[0]);
        fileWatchdog.start();

        while (true) {
            Thread.sleep(5000);
            System.out.println("Time: " + new Date());
            try {
                Process process = Runtime.getRuntime().exec("tasklist");
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(process.getInputStream()));
                String item;
                while ((item = bufferedReader.readLine()) != null) {
                    System.out.println(item);
                }
                process.destroyForcibly();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
