package utils.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FirefoxDriverFactory implements BrowserDriverFactory {
    @Override
    public WebDriver createDriver() {
        return new FirefoxDriver();
    }
}
