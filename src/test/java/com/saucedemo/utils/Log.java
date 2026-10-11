package com.saucedemo.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/** Tiny wrapper over java.util.logging: one-line logs to the console and to target/logs/test-run.log. */
public final class Log {

    private static final Logger LOGGER = Logger.getLogger("saucedemo");

    static {
        Formatter formatter = new Formatter() {
            private final DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");

            @Override
            public String format(LogRecord r) {
                return String.format("[%s] %-7s %s%n", LocalTime.now().format(time), r.getLevel(), r.getMessage());
            }
        };
        LOGGER.setUseParentHandlers(false);
        ConsoleHandler console = new ConsoleHandler();
        console.setFormatter(formatter);
        LOGGER.addHandler(console);
        try {
            Path dir = Files.createDirectories(Path.of("target", "logs"));
            FileHandler file = new FileHandler(dir.resolve("test-run.log").toString(), true);
            file.setFormatter(formatter);
            LOGGER.addHandler(file);
        } catch (IOException e) {
            LOGGER.warning("File logging disabled: " + e.getMessage());
        }
        // Keep Selenium's own logging quiet unless something is wrong
        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
    }

    private Log() {
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void error(String message) {
        LOGGER.severe(message);
    }
}
