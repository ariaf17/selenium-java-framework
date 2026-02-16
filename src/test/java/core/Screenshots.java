package core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class Screenshots {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private Screenshots() {}

    public static Path savePng(WebDriver driver, String name) {
        try {
            String safe = sanitize(name);
            String stamp = LocalDateTime.now().format(TS);

            Path dir = Path.of("target", "screenshots");
            Files.createDirectories(dir);

            Path file = dir.resolve(stamp + "_" + safe + ".png");
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(file, png);
            return file;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save screenshot: " + name, e);
        }
    }

    public static byte[] asPngBytes(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    private static String sanitize(String name) {
        if (name == null || name.isBlank()) return "screenshot";
        return name.trim().replaceAll("[^a-zA-Z0-9._-]+", "_");
    }
}
