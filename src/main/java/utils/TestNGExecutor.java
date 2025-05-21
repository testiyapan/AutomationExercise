package utils;

import org.testng.TestNG;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;

public class TestNGExecutor {

    public static void runSuite(String xmlPath) {
        TestNG testng = new TestNG();
        File testClassesDir = new File(PathsUtil.getTestClassesDir());

        try {
            URLClassLoader classLoader = new URLClassLoader(
                    new URL[]{testClassesDir.toURI().toURL()},
                    Thread.currentThread().getContextClassLoader()
            );
            Thread.currentThread().setContextClassLoader(classLoader);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        testng.setTestSuites(Collections.singletonList(xmlPath));
        testng.run();
    }
}
