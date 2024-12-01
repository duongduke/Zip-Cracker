package org.example;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PasswordCracker {

    private final File zipFile;
    private final List<String> passwords;
    private final int threadCount;

    public PasswordCracker(File zipFile, List<String> passwords, int threadCount) {
        this.zipFile = zipFile;
        this.passwords = passwords;
        this.threadCount = threadCount;
    }

    public void crackPassword() {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicBoolean found = new AtomicBoolean(false);

        for (String password : passwords) {
            if (found.get()) break; // Nếu đã tìm thấy mật khẩu thì dừng
            executor.submit(() -> {
                if (found.get()) return;

                try {
                    System.out.println("Thử mật khẩu: " + password);
                    ZipFile zip = new ZipFile(zipFile);
                    zip.setPassword(password.toCharArray());
                    zip.extractAll("output"); // Thư mục giải nén
                    System.out.println("Mật khẩu đúng là: " + password);
                    found.set(true);
                    executor.shutdownNow(); // Dừng mọi tác vụ
                } catch (ZipException e) {
                    // Nếu sai mật khẩu, tiếp tục
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.println("Quá trình thử mật khẩu bị gián đoạn!");
        }

        if (!found.get()) { // Kiểm tra trạng thái sau khi tất cả luồng kết thúc
            System.out.println("Không tìm thấy mật khẩu đúng.");
        }
    }
}
