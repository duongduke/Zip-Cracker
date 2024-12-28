package org.example;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;

public class ProcessAffinityManager {
    // Định nghĩa interface để giao tiếp với Windows API
    public interface Kernel32Extended extends StdCallLibrary {
        Kernel32Extended INSTANCE = Native.load("kernel32", Kernel32Extended.class);

        WinNT.HANDLE GetCurrentThread();

        boolean SetThreadAffinityMask(WinNT.HANDLE thread, long mask);
    }

    public static void setThreadAffinity(int threadIndex) {
        if (!Platform.isWindows()) {
            return;
        }

        try {
            // Lấy CPU tương ứng với thread index
            int cpuNumber = Config.SELECTED_CORES[threadIndex];
            // Tạo mask bằng cách dịch bit sang trái theo số CPU
            long mask = 1L << cpuNumber;

            WinNT.HANDLE thread = Kernel32Extended.INSTANCE.GetCurrentThread();
            boolean success = Kernel32Extended.INSTANCE.SetThreadAffinityMask(thread, mask);

            if (!success) {
                System.out.println("Không thể set thread affinity cho CPU " + cpuNumber);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi set thread affinity: " + e.getMessage());
        }
    }
}