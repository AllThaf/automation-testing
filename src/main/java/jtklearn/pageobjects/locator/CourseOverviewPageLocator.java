package jtklearn.pageobjects.locator;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CourseOverviewPageLocator {

    // Input kode pendaftaran
    @FindBy(xpath = "//input[contains(@placeholder, 'Kode') or contains(@placeholder, 'pendaftaran')]")
    public WebElement codeField;

    // Tombol Daftar
    @FindBy(xpath = "//button[contains(text(),'Daftar')]")
    public WebElement registerButton;

    // Pesan popup SweetAlert
    @FindBy(id = "swal2-html-container")
    public WebElement popupMessage;

    // Container popup SweetAlert
    @FindBy(css = ".swal2-container")
    public WebElement popupContainer;

}

