package app;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class SQLReader {
    @NotNull public static String readSQL(@NotNull Path path) {
        if (Files.isRegularFile(path)) {
            try {
                return Files.readString(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Что-то не так с SQL-файлом");
    }
}
