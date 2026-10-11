package com.saucedemo.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotUtil {

    private ScreenshotUtil() {
    }

    /** Saves a PNG to target/screenshots and returns its path, or null if it could not be taken. */
    public static Path capture(WebDriver driver, String name) {
        if (driver == null) {
            return null;
        }
        try {
            Path dir = Files.createDirectories(Path.of("target", "screenshots"));
            Path file = dir.resolve(name + ".png");
            Files.write(file, ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
            return file;
        } catch (IOException | RuntimeException e) {
            Log.error("Screenshot failed: " + e.getMessage());
            return null;
        }
    }
}
