import base.BaseTest;
import expecteds.HomePageExpected;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;

public class HomePageTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod
    public void setUp() {
        homePage = new HomePage(driver);
        Assert.assertNotNull(driver, "Driver is null, the test cannot be started!");
    }

    @Test
    public void testHomePageLoadsSuccessfully() {
        Assert.assertTrue(homePage.isHomePageOpened(), "Home Page not laodedi!");
    }

    @Test
    public void testHomePageTitleSuccessfully() {
        Assert.assertEquals(homePage.getHomePageTitle(), HomePageExpected.EXPECTED_TITLE);
    }

    @Test
    public void testNavigationMenuIsVisible() {
        Assert.assertTrue(homePage.isNavBarVisible(), "The navigation menu is visible!");
    }
}
