package pages;

import expecteds.HomePageExpected;
import interfaces.IHomePage;
import locators.HomePageLocators;
import org.openqa.selenium.WebDriver;

public class HomePage extends PageObject implements IHomePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isHomePageOpened() {
        return getPageTitle().contains(HomePageExpected.EXPECTED_TITLE);
    }

    @Override
    public String getHomePageTitle() {
        return getPageTitle();
    }

    @Override
    public boolean isNavBarVisible() {
        return isElementVisible(HomePageLocators.navigationMenu);
    }

}
