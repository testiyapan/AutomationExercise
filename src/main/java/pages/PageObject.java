package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;


public abstract class PageObject {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public PageObject(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver nesnesi null olamaz!");
        }
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void switchToNewTab() {
        String originalTab = driver.getWindowHandle();
        for (String tab : driver.getWindowHandles()) {
            if (!tab.equals(originalTab)) {
                driver.switchTo().window(tab);
                break;
            }
        }
    }

    public void closeCurrentTab() {
        driver.close();
    }

    public void clickElementByXpath(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        driver.findElement(locator).click();
    }

    public void waitUntilVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    public List<WebElement> getElements(By locator) {
        waitUntilVisible(locator);
        return driver.findElements(locator);
    }

    public boolean verifyElementText(By locator, String expectedText) {
        try {
            String actualText = driver.findElement(locator).getText().trim();
            return actualText.equals(expectedText);
        } catch (NoSuchElementException e) {
            System.out.println("Element not found: " + locator);
            return false;
        }
    }

    public void waitForUrlToContain(String expectedUrlPart, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.urlContains(expectedUrlPart));
    }

    public void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollAndClick(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void clickWithJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void clickWithJS(By locator) {
        WebElement element = driver.findElement(locator);
        clickWithJS(element);
    }

    public void hoverOverElement(By locator) {
        WebElement element = waitUntilVisible(locator, 5);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);

        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    public WebElement waitUntilVisible(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitAndClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    public void waitUntilElementIsVisible(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public boolean isElementVisible(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementExist(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public List<String> getTextList(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public void scrollBy(int pixels) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, arguments[0]);", pixels);
    }

    public void scrollUntilElementVisible(By locator, int maxScrollAttempts) {
        int attempts = 0;
        while (attempts < maxScrollAttempts) {
            if (isElementVisible(locator)) {
                System.out.println("Element is visible");
                return;
            }
            scrollBy(500);
            attempts++;
            System.out.println("Scrolling times " + attempts);
        }
        throw new RuntimeException("Element not visible " + locator);
    }

    // PageObject.java
    public String getPageTitle() {
        return driver.getTitle();
    }

}
