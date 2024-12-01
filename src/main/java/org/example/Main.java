package org.example;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String zipFileExtension = ".zip";

        try {
            // Lấy thư mục resources trong classpath
            Path resourcePath = Paths.get(Main.class.getClassLoader().getResource("").toURI());

            // Duyệt qua các file trong thư mục resources
            try (Stream<Path> paths = Files.walk(resourcePath)) {
                paths.filter(Files::isRegularFile) // Chỉ lấy file thông thường
                        .filter(path -> path.toString().endsWith(zipFileExtension)) // Lọc file ZIP
                        .forEach(path -> {
                            try {
                                System.out.println("Đang xử lý file: " + path.getFileName());
                                File zipFile = path.toFile();
                                List<String> passwords = PasswordGenerator.generateFourDigitPasswords();
                                int threadCount = 10;

                                PasswordCracker cracker = new PasswordCracker(zipFile, passwords, threadCount);
                                cracker.crackPassword();
                            } catch (Exception e) {
                                System.out.println("Lỗi khi xử lý file ZIP: " + e.getMessage());
                            }
                        });
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi duyệt qua thư mục resources: " + e.getMessage());
        }
    }
}
