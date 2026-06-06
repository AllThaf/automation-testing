package jtklearn.pageobjects.actions;

import jtklearn.pageobjects.locator.LoginPageLocator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPageActions {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final LoginPage page;

    public LoginPageActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        page = new LoginPage();
        PageFactory.initElements(driver, page);
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(page.emailField));

        page.emailField.clear();
        page.emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(page.passwordField));

        page.passwordField.clear();
        page.passwordField.sendKeys(password);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(page.loginButton));
        page.loginButton.click();
    }

    public String getErrorMessage() {
        wait.until(ExpectedConditions.visibilityOf(page.errorMessage));
        return page.errorMessage.getText();
    }

}

