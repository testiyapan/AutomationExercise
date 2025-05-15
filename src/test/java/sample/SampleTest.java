package sample;

import io.qameta.allure.*;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;

public class SampleTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod
    @Severity(SeverityLevel.CRITICAL)
    public void setUp() {
        homePage = new HomePage(driver);
        Allure.step("Driver running...");
        Assert.assertNotNull(driver, "Driver is null, the test cannot be started!");
    }

    @Test
    @Description("Homepage yüklenme testi")
    @Severity(SeverityLevel.CRITICAL)
    public void testHomePageLoadsSuccessfully() {
        Allure.step("Anasayfa açılıyor");
        Assert.assertTrue(homePage.isHomePageOpened(), "Home Page not laoded!");
    }

    @Test
    @Description("Homepage yüklenme testi")
    @Severity(SeverityLevel.CRITICAL)
    public void testHomePageTitleSuccessfully() {
        Allure.step("Anasayfanin basligi aliniyor");
        Assert.assertEquals(homePage.getHomePageTitle(), "HomePageExpected.EXPECTED_TITLE");
    }
}



