package jtklearn.pageobjects.locator;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class DashboardPageLocator {

    @FindBy(css = ".navbar-nav .nav-link")
    public List<WebElement> navbarMenus;

    @FindBy(css = ".nav-name.dropdown > a.nav-link")
    public WebElement profileDropdown;
}