package org.example;

public class LogManager {
    private static final int COLUMN_WIDTH = 40;
    private static final String FORMAT = "%-" + COLUMN_WIDTH + "s";
    private final int threadCount;
    private boolean isFirstLog = true;
    private final String[] currentPasswords;

    public LogManager(int threadCount) {
        this.threadCount = threadCount;
        this.currentPasswords = new String[threadCount];
    }

    public synchronized void log(int threadIndex, String message) {
        if (isFirstLog) {
            // In thông tin file và thread lần đầu
            System.out.println("Dang xu ly file...");
            // In thông tin gán CPU cho từng thread
            for (int i = 0; i < threadCount; i++) {
                System.out.println("Thread " + i + " da duoc gan vao CPU " + Config.SELECTED_CORES[i]);
            }
            System.out.println(); // Dòng trống

            // In các dòng cố định cho từng thread
            for (int i = 0; i < threadCount; i++) {
                System.out.println("Thread " + i + " thu mat khau: ");
            }
            isFirstLog = false;
        } else {
            // Lưu mật khẩu hiện tại của thread
            currentPasswords[threadIndex] = message;

            // Di chuyển con trỏ lên đầu vùng hiển thị mật khẩu
            System.out.print("\033[" + threadCount + "A");

            // In lại tất cả các dòng với mật khẩu mới
            for (int i = 0; i < threadCount; i++) {
                String password = currentPasswords[i] != null ? currentPasswords[i] : "";
                System.out.println("Thread " + i + " thu mat khau: " + password);
            }

            // Di chuyển con trỏ xuống cuối
            System.out.print("\033[" + threadCount + "B");
        }
    }

    public synchronized void printResult(String message) {
        System.out.println("\n" + "=".repeat(COLUMN_WIDTH));
        System.out.println(message);
    }
}