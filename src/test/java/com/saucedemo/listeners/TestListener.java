package com.saucedemo.listeners;

import java.nio.file.Path;
import java.util.Arrays;

import org.testng.ITestListener;
import org.testng.ITestResult;

import com.saucedemo.utils.DriverFactory;
import com.saucedemo.utils.Log;
import com.saucedemo.utils.ScreenshotUtil;

/** Logs each test's outcome and takes a screenshot when a test fails. */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        Log.info("START  " + name(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Log.info("PASS   " + name(result) + " (" + (result.getEndMillis() - result.getStartMillis()) + " ms)");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // First line only; the full stack trace is in the TestNG/Surefire reports
        String reason = String.valueOf(result.getThrowable()).lines().findFirst().orElse("");
        Log.error("FAIL   " + name(result) + " -> " + reason);
        Path shot = ScreenshotUtil.capture(DriverFactory.get(), name(result).replaceAll("[^A-Za-z0-9_.-]", "_"));
        if (shot != null) {
            Log.error("Screenshot saved: " + shot.toAbsolutePath());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Log.info("SKIP   " + name(result));
    }

    private static String name(ITestResult result) {
        String params = result.getParameters().length > 0 ? Arrays.toString(result.getParameters()) : "";
        return result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName() + params;
    }
}
