package org.example;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;

public class ProcessAffinityManager {
    private static int[] selectedCores;

    public static void setSelectedCores(int[] cores) {
        selectedCores = cores;
    }

    // Định nghĩa interface để giao tiếp với Windows API
    public interface Kernel32Extended extends StdCallLibrary {
        Kernel32Extended INSTANCE = Native.load("kernel32", Kernel32Extended.class);

        WinNT.HANDLE GetCurrentThread();

        boolean SetThreadAffinityMask(WinNT.HANDLE thread, long mask);
    }

    public static void setThreadAffinity(int threadIndex) {
        if (selectedCores != null && threadIndex < selectedCores.length) {
            int core = selectedCores[threadIndex];
            if (!Platform.isWindows()) {
                return;
            }

            try {
                // Tạo mask bằng cách dịch bit sang trái theo số CPU
                long mask = 1L << core;

                WinNT.HANDLE thread = Kernel32Extended.INSTANCE.GetCurrentThread();
                boolean success = Kernel32Extended.INSTANCE.SetThreadAffinityMask(thread, mask);

                if (!success) {
                    System.out.println("Không thể set thread affinity cho CPU " + core);
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi set thread affinity: " + e.getMessage());
            }
        }
    }
}