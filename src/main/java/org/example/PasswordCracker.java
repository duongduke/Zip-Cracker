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
    private volatile boolean shouldStop = false;
    private volatile boolean isPaused = false;
    private volatile String foundPassword = null;
    private long startTime;

    public PasswordCracker(File zipFile, int threadCount) {
        this.zipFile = zipFile;
        this.threadCount = threadCount;
        this.passwordsByThread = PasswordGenerator.generatePasswordsByThread(threadCount);
    }

    protected void log(int threadIndex, String message) {
        System.out.println("Thread " + threadIndex + ": " + message);
    }

    protected void logResult(String message) {
        System.out.println(message);
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public void crackPassword() {
        startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicBoolean found = new AtomicBoolean(false);

        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            final List<String> threadPasswords = passwordsByThread.get(i);

            executor.submit(() -> {
                ProcessAffinityManager.setThreadAffinity(threadIndex);
                ZipFile zip = new ZipFile(zipFile);

                try {
                    for (String password : threadPasswords) {
                        try {
                            while (isPaused) {
                                Thread.sleep(100);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }

                        if (found.get() || shouldStop) {
                            return;
                        }

                        try {
                            log(threadIndex, "Thử mật khẩu: " + password);
                            zip.setPassword(password.toCharArray());
                            zip.extractAll("output");

                            found.set(true);
                            shouldStop = true;
                            foundPassword = password;
                            logResult("Mật khẩu đúng là: " + password);
                            executor.shutdownNow();
                            return;
                        } catch (ZipException e) {
                            continue;
                        }
                    }
                } finally {
                    try {
                        zip.close();
                    } catch (IOException e) {
                        log(threadIndex, "Lỗi khi đóng file ZIP: " + e.getMessage());
                    }
                }
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.MINUTES)) {
                executor.shutdownNow();
                logResult("Quá trình tìm kiếm đã bị hủy do vượt quá thời gian.");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            logResult("Quá trình thử mật khẩu bị gián đoạn!");
        }

        if (found.get()) {
            long duration = System.currentTimeMillis() - startTime;
            String timeStr = formatDuration(duration);
            logResult("Mật khẩu đúng là: " + foundPassword + "\nThời gian chạy: " + timeStr);
        } else if (!executor.isTerminated()) {
            return;
        } else {
            long duration = System.currentTimeMillis() - startTime;
            String timeStr = formatDuration(duration);
            logResult("Không tìm thấy mật khẩu đúng.\nThời gian chạy: " + timeStr);
        }
    }

    private String formatDuration(long duration) {
        long seconds = duration / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;

        if (minutes > 0) {
            return String.format("%02d:%02d phút", minutes, seconds);
        } else {
            return String.format("%02d giây", seconds);
        }
    }
}
