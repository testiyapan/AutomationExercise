package utils.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class EdgeDriverFactory implements BrowserDriverFactory {
    @Override
    public WebDriver createDriver() {
        return new EdgeDriver();
    }
}
