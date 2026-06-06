package jtklearn.pageobjects.locator;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPageLocator {

    // Field email
    @FindBy(css = "input[type='email']")
    public WebElement emailField;

    // Field password
    @FindBy(css = "input[type='password']")
    public WebElement passwordField;

    // Tombol login
    @FindBy(xpath = "//button[contains(text(),'Masuk')]")
    public WebElement loginButton;

    // Pesan error SweetAlert
    @FindBy(id = "swal2-html-container")
    public WebElement errorMessage;

}
