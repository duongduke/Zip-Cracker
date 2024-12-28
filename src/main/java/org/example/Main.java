package org.example;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String zipFileExtension = ".zip";

        try {
            Path resourcePath = Paths.get(Main.class.getClassLoader().getResource("").toURI());

            try (Stream<Path> paths = Files.walk(resourcePath)) {
                paths.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(zipFileExtension))
                        .forEach(path -> {
                            try {
                                File zipFile = path.toFile();
                                PasswordCracker cracker = new PasswordCracker(zipFile, Config.THREAD_COUNT);
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
