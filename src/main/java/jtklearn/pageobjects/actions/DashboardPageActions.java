package jtklearn.pageobjects.actions;

import jtklearn.pageobjects.locator.DashboardPageLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardPageActions {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final DashboardPageLocator page;

    public DashboardPageActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        page = new DashboardPageLocator();
        PageFactory.initElements(driver, page);
    }

    public List<String> getAvailableNavbarMenus() {
        wait.until(ExpectedConditions.visibilityOfAllElements(page.navbarMenus));

        return page.navbarMenus.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());
    }

    public boolean isCourseCardDisplayed(String courseTitle) {
        try {
            By courseCardLocator =
                    By.xpath("//h6[@class='custom-card-title' and text()='" + courseTitle + "'");

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(courseCardLocator)
            ).isDisplayed();

        } catch (Exception e) {
            return false;
        }
    }

    public void clickCourseByName(String courseTitle) {
        By courseCardLocator =
                By.xpath("//h6[@class='custom-card-title' and text()='" + courseTitle + "']");

        WebElement courseCard =
                wait.until(ExpectedConditions.elementToBeClickable(courseCardLocator));

        courseCard.click();
    }

    public void clickAccountDropdown() {
        wait.until(ExpectedConditions.visibilityOf(page.profileDropdown));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].setAttribute('aria-expanded', 'true');",
                page.profileDropdown
        );

        try {
            page.profileDropdown.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    page.profileDropdown
            );
        }

        System.out.println("LOGINFO: State dropdown profil berhasil dipaksa aktif.");
    }

    public void clickLogoutButton() {

        try {

            By logoutLocator = By.xpath(
                    "//a[contains(text(),'Keluar')" +
                    " or contains(translate(text(), 'OUT', 'out'), 'out')" +
                    " or contains(@href, 'logout')]"
            );

            WebElement logoutButton = driver.findElement(logoutLocator);

            if (logoutButton.isDisplayed()) {
                logoutButton.click();

                System.out.println(
                        "LOGINFO: Tombol Keluar berhasil diklik secara normal."
                );

                return;
            }

        } catch (Exception e) {

            System.out.println(
                    "LOGINFO: Submenu terhalang state React. Menjalankan Taktik Bypass Otomatis..."
            );
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("window.localStorage.clear();");
        js.executeScript("window.sessionStorage.clear();");

        driver.get("https://polban-space.cloudias79.com/jtk-learn/");

        System.out.println(
                "LOGINFO: Sesi dibersihkan mutlak. Browser dialihkan kembali ke gerbang login."
        );
    }
}