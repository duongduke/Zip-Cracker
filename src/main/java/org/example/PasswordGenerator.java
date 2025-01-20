package org.example;

import java.util.ArrayList;
import java.util.List;

public class PasswordGenerator {
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBER_CHARS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    private static final int BLOCK_SIZE = 100000;

    public static List<List<String>> generatePasswordsByThread(int threadCount) {
        // Giữ lại phương thức cũ để tương thích ngược
        return generatePasswordsByThread(threadCount, 4, true, false, true, false);
    }

    public static List<List<String>> generatePasswordsByThread(
            int threadCount, int passwordLength, boolean useLowercase,
            boolean useUppercase, boolean useNumbers, boolean useSpecial) {

        // Tạo chuỗi ký tự dựa trên tùy chọn
        StringBuilder chars = new StringBuilder();
        if (useLowercase)
            chars.append(LOWERCASE_CHARS);
        if (useUppercase)
            chars.append(UPPERCASE_CHARS);
        if (useNumbers)
            chars.append(NUMBER_CHARS);
        if (useSpecial)
            chars.append(SPECIAL_CHARS);

        if (chars.length() == 0) {
            // Nếu không có ký tự nào được chọn, sử dụng mặc định
            chars.append(LOWERCASE_CHARS).append(NUMBER_CHARS);
        }

        String CHARS = chars.toString();
        int CHARS_LENGTH = CHARS.length();
        long TOTAL_COMBINATIONS = (long) Math.pow(CHARS_LENGTH, passwordLength);

        List<List<String>> passwordsByThread = new ArrayList<>(threadCount);
        for (int i = 0; i < threadCount; i++) {
            passwordsByThread.add(new ArrayList<>());
        }

        // Phân phối các block cho các thread
        long totalBlocks = (TOTAL_COMBINATIONS + BLOCK_SIZE - 1) / BLOCK_SIZE;
        for (long blockIndex = 0; blockIndex < totalBlocks; blockIndex++) {
            int threadIndex = (int) (blockIndex % threadCount);
            long startIndex = blockIndex * BLOCK_SIZE;
            long endIndex = Math.min(startIndex + BLOCK_SIZE, TOTAL_COMBINATIONS);

            for (long i = startIndex; i < endIndex; i++) {
                passwordsByThread.get(threadIndex).add(
                        generatePassword(i, CHARS, passwordLength));
            }
        }

        return passwordsByThread;
    }

    private static String generatePassword(long index, String chars, int length) {
        StringBuilder password = new StringBuilder();
        long remaining = index;
        int charsLength = chars.length();

        for (int i = 0; i < length; i++) {
            int charIndex = (int) (remaining % charsLength);
            password.insert(0, chars.charAt(charIndex));
            remaining /= charsLength;
        }

        return password.toString();
    }
}
