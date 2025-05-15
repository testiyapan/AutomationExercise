package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.*;
import utils.factory.BrowserDriverFactory;
import utils.factory.ChromeDriverFactory;
import utils.factory.EdgeDriverFactory;
import utils.factory.FirefoxDriverFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static ExtentReports extent;
    protected static ExtentSparkReporter spark;
    protected static ThreadLocal<ExtentTest> testReport = new ThreadLocal<>();

    @BeforeSuite
    public void setupReportSuite() {
//        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String timestamp = LocalDateTime.now(ZoneId.of("Europe/Istanbul")).format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String extentReportDir = PathsUtil.getReportPath();
        new File(extentReportDir).mkdirs();

        String allureResultPath = "allure-results/" + timestamp;
        new File(allureResultPath).mkdirs();
        System.setProperty("allure.results.directory", allureResultPath);

        // ExtentReporter başlat
        if (extent == null) {
            spark = new ExtentSparkReporter(extentReportDir + "report-" + timestamp + ".html");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            logger.info("Extent report initialized.");
            logger.info("Allure results directory: " + allureResultPath);
        }
    }

    @BeforeClass
    @Parameters("browser")
    public void setupDriver(@Optional("chrome") String browserParam) {
        String systemBrowser = System.getProperty("browser");
        String browser = systemBrowser != null ? systemBrowser : browserParam;

        if (browser == null) {
            throw new IllegalStateException("Browser info is missing.");
        }

        logger.info("Starting driver for browser: {}", browser);

        BrowserDriverFactory driverFactory;
        switch (browser.toLowerCase()) {
            case "firefox":
                driverFactory = new FirefoxDriverFactory();
                break;
            case "edge":
                driverFactory = new EdgeDriverFactory();
                break;
            case "chrome":
            default:
                driverFactory = new ChromeDriverFactory();
                break;
        }

        driver = driverFactory.createDriver();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("implicitWait"))));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("explicitWait"))));

        String baseUrl = ConfigReader.getProperty("baseUrl");
        logger.info("Opening URL: {}", baseUrl);
        driver.get(baseUrl);
    }

    @BeforeMethod
    public void startTest(Method method) {
        ExtentTest test = extent.createTest(method.getName());
        testReport.set(test);
    }

    @AfterMethod
    public void evaluateTestResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE || result.getStatus() == ITestResult.SKIP) {
            String status = result.getStatus() == ITestResult.FAILURE ? "FAILED" : "SKIPPED";
            logger.warn("Test {}: {}", status, result.getName());

            File screenshotFile = takeScreenshot(result.getName());

            if (screenshotFile != null) {
                testReport.get().fail(result.getThrowable());
                testReport.get().addScreenCaptureFromPath(screenshotFile.getAbsolutePath());

                try (InputStream is = new FileInputStream(screenshotFile)) {
                    Allure.addAttachment("Screenshot - " + result.getName(), "image/png", is, ".png");
                } catch (IOException e) {
                    logger.error("Error attaching screenshot to Allure", e);
                }
            } else {
                logger.error("Screenshot could not be captured for test: {}", result.getName());
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test passed: {}", result.getName());
            testReport.get().pass("Test passed.");
        }
    }


    @AfterClass
    public void tearDownDriver() {
        if (driver != null) {
            driver.quit();
            logger.info("WebDriver kapatıldı");
        }
    }

    @AfterSuite
    public void flushReportSuite() {
        if (extent != null) {
            extent.flush();
            logger.info("Extent report flushed.");
        }

        File allureDir = new File(System.getProperty("allure.results.directory"));
        if (allureDir.exists() && allureDir.isDirectory() && Objects.requireNonNull(allureDir.list()).length == 0) {
            if (allureDir.delete()) {
                logger.info("Boş allure-results klasörü silindi: " + allureDir.getAbsolutePath());
            } else {
                logger.warn("Boş allure-results klasörü silinemedi: " + allureDir.getAbsolutePath());
            }
        }
    }


    private File takeScreenshot(String testName) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            File screenshotDir = new File(PathsUtil.getScreenshotPath());
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            File dest = new File(screenshotDir, testName + ".png");
            FileUtils.copyFile(screenshot, dest);
//            testReport.get().addScreenCaptureFromPath(dest.getAbsolutePath());
            return dest;
        } catch (IOException e) {
            logger.error("Error while taking screenshot", e);
            return null;
        }
    }

}
