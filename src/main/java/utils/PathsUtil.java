package utils;

public class PathsUtil {
    private static final String ROOT_DIR = System.getProperty("user.dir");

    public static String getRootDir() {
        return ROOT_DIR;
    }

    public static String getLogFilePath() {
        return ROOT_DIR + "/logs/test.log";
    }

    public static String getReportPath() {
        return ROOT_DIR + "/reports/";
    }

    public static String getScreenshotPath() {
        return ROOT_DIR + "/screenshots/";
    }
    public static String getTestSourceDir() {
        return ROOT_DIR + "/src/test/java";
    }

    public static String getAllureResultsPath(String timestamp) {
        return ROOT_DIR + "/allure-results/" + timestamp;
    }

    public static String getTestNgXmlDirectory() {
        return ROOT_DIR + "/testNgXmlFiles";
    }

    public static String getGeneratedTestNgXmlPath() {
        return getTestNgXmlDirectory() + "/generated_testng.xml";
    }
    public static String getTestClassesDir() {
        return ROOT_DIR + "/target/test-classes";
    }

}
