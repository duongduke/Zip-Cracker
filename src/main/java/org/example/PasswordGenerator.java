package org.example;

import java.util.ArrayList;
import java.util.List;

public class PasswordGenerator {
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 4;
    private static final int CHARS_LENGTH = CHARS.length();
    private static final int TOTAL_COMBINATIONS = (int) Math.pow(CHARS_LENGTH, PASSWORD_LENGTH);
    private static final int BLOCK_SIZE = 1000; // Kích thước block nhỏ

    public static List<List<String>> generatePasswordsByThread(int threadCount) {
        List<List<String>> passwordsByThread = new ArrayList<>(threadCount);
        for (int i = 0; i < threadCount; i++) {
            passwordsByThread.add(new ArrayList<>());
        }
        // tạo block
        int totalBlocks = (TOTAL_COMBINATIONS + BLOCK_SIZE - 1) / BLOCK_SIZE;

        // Phân phối các block cho các thread theo kiểu xen kẽ
        for (int blockIndex = 0; blockIndex < totalBlocks; blockIndex++) {
            int threadIndex = blockIndex % threadCount;
            int startIndex = blockIndex * BLOCK_SIZE;
            int endIndex = Math.min(startIndex + BLOCK_SIZE, TOTAL_COMBINATIONS);

            // Thêm tất cả mật khẩu trong block vào thread tương ứng
            for (int i = startIndex; i < endIndex; i++) {
                passwordsByThread.get(threadIndex).add(generatePassword(i));
            }
        }

        return passwordsByThread;
    }

    private static String generatePassword(int index) {
        StringBuilder password = new StringBuilder();
        int remaining = index;

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int charIndex = remaining % CHARS_LENGTH;
            password.insert(0, CHARS.charAt(charIndex));
            remaining /= CHARS_LENGTH;
        }

        return password.toString();
    }
}
