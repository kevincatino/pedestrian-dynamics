package ar.edu.itba.ss.step.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface IO {
    static String readFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        String content;
        content = Files.readString(filePath);

        return content;
    }
}
