package utils.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeDriverFactory implements BrowserDriverFactory {
    @Override
    public WebDriver createDriver() {
        return new ChromeDriver();
    }
}
