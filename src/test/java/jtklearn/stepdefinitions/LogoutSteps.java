package jtklearn.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jtklearn.pageobjects.actions.DashboardPageActions;
import jtklearn.pageobjects.actions.LoginPageActions;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

public class LogoutSteps {

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    @Given("Pengguna telah mengakses halaman login")
    public void pengguna_telah_mengakses_halaman_login() {
        driver().get("https://polban-space.cloudias79.com/jtk-learn/");
    }

    @Given("Pengguna login sebagai Pelajar dengan email {string} dan password {string}")
    public void pengguna_login_sebagai_pelajar_dengan_email_dan_password(String email, String password) {
        driver().get("https://polban-space.cloudias79.com/jtk-learn/");
        LoginPageActions loginPage = new LoginPageActions(driver());
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
    }

    @When("Pengguna menekan tombol dropwdown nama akun")
    public void pengguna_menekan_tombol_dropdown_nama_akun() {
        DashboardPageActions dashboardPage = new DashboardPageActions(driver());
        dashboardPage.clickAccountDropdown();
    }

    @And("Pengguna menekan tombol keluar")
    public void pengguna_menekan_tombol_keluar() {
        DashboardPageActions dashboardPage = new DashboardPageActions(driver());
        dashboardPage.clickLogoutButton();
    }

    @Then("Pengguna berhasil logout")
    public void pengguna_berhasil_logout() {
        assertThat(driver().getCurrentUrl())
                .as("Pengguna harus sudah logout dan tidak di dashboard")
                .doesNotContain("dashboard");
    }

    @And("Pengguna diarahkan ke halaman login")
    public void pengguna_diarahkan_ke_halaman_login() {
        assertThat(driver().getCurrentUrl())
                .as("Pengguna berada di halaman login")
                .contains("jtk-learn")
                .doesNotContain("dashboard");
    }
}
