package jtklearn.pageobjects.actions;

import jtklearn.pageobjects.CourseOverviewPageLocator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CourseOverviewPageActions {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final CourseOverviewPage page;

    public CourseOverviewPageActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        page = new CourseOverviewPageLocator();
        PageFactory.initElements(driver, page);
    }

    public void enterRegistrationCode(String code) {
        wait.until(ExpectedConditions.visibilityOf(page.codeField));
        page.codeField.clear();
        page.codeField.sendKeys(code);
    }

    public void clickRegister() {
        wait.until(ExpectedConditions.elementToBeClickable(page.registerButton));
        page.registerButton.click();
    }

    public boolean isPopupModalDisplayed() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOf(page.popupContainer))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPopupText() {
        wait.until(ExpectedConditions.visibilityOf(page.popupMessage));
        return page.popupMessage.getText();
    }
}