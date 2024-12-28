package org.example;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PasswordCracker {

    private final File zipFile;
    private final List<List<String>> passwordsByThread;
    private final int threadCount;
    private final LogManager logManager;
    private volatile boolean shouldStop = false;

    public PasswordCracker(File zipFile, int threadCount) {
        this.zipFile = zipFile;
        this.threadCount = threadCount;
        this.passwordsByThread = PasswordGenerator.generatePasswordsByThread(threadCount);
        this.logManager = new LogManager(threadCount);
    }

    public void crackPassword() {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicBoolean found = new AtomicBoolean(false);

        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            final List<String> threadPasswords = passwordsByThread.get(i);

            executor.submit(() -> {
                ProcessAffinityManager.setThreadAffinity(threadIndex % 16);
                ZipFile zip = new ZipFile(zipFile);

                try {
                    for (String password : threadPasswords) {
                        if (found.get() || shouldStop) {
                            return;
                        }

                        try {
                            logManager.log(threadIndex, "Thử mật khẩu: " + password);
                            zip.setPassword(password.toCharArray());
                            zip.extractAll("output");

                            // Nếu không có exception tức là mật khẩu đúng
                            found.set(true);
                            shouldStop = true;
                            logManager.printResult("Mật khẩu đúng là: " + password);
                            executor.shutdownNow();
                            return;
                        } catch (ZipException e) {
                            // Tiếp tục nếu sai mật khẩu
                            continue;
                        }
                    }
                } finally {
                    try {
                        zip.close();
                    } catch (IOException e) {
                        logManager.log(threadIndex, "Lỗi khi đóng file ZIP: " + e.getMessage());
                    }
                }
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            logManager.printResult("Quá trình thử mật khẩu bị gián đoạn!");
        }

        if (!found.get()) {
            logManager.printResult("Không tìm thấy mật khẩu đúng.");
        }
    }
}
