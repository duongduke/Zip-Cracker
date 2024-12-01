package org.example;

import java.util.stream.IntStream;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordGenerator {
    public static List<String> generateFourDigitPasswords() {
        return IntStream.range(0, 10000)
                .mapToObj(i -> String.format("%04d", i))
                .collect(Collectors.toList());
    }
}
