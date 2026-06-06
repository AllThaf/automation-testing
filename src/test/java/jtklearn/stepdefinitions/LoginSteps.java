package jtklearn.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jtklearn.pageobjects.actions.LoginPageActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private WebDriver driver;
    private LoginPageActions loginPage;

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // aktifkan jika ingin headless
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ─── Background Steps ───────────────────────────────────────────────────────

    @Given("Pengguna telah mengakses halaman login aplikasi JTK Learn")
    public void pengguna_telah_mengakses_halaman_login() {
        driver.get("https://polban-space.cloudias79.com/jtk-learn/");
        loginPage = new LoginPageActions(driver);
    }

    @And("pengguna mengakses URL {string}")
    public void pengguna_mengakses_url(String url) {
        driver.get(url);
        loginPage = new LoginPageActions(driver);
    }

    @And("pengguna belum memiliki session login aktif")
    public void pengguna_belum_memiliki_session_login_aktif() {
        // Tidak ada sesi aktif — sudah terpenuhi karena browser baru dibuka
    }

    // ─── TC 1.0.2 — Login gagal: username tidak terdaftar ───────────────────────

    @When("sistem menampilkan halaman login")
    public void sistem_menampilkan_halaman_login() {
        assertThat(driver.getCurrentUrl())
                .as("URL harus mengarah ke halaman login")
                .contains("jtk-learn");
    }

    @And("pengguna mengisi field {string} dengan {string}")
    public void pengguna_mengisi_field_dengan(String fieldName, String value) {
        switch (fieldName) {
            case "Email":
                loginPage.enterEmail(value);
                break;
            case "Password":
                loginPage.enterPassword(value);
                break;
            default:
                throw new IllegalArgumentException("Field tidak dikenal: " + fieldName);
        }
    }

    @And("pengguna menekan tombol {string}")
    public void pengguna_menekan_tombol(String buttonName) {
        if ("Masuk".equals(buttonName)) {
            loginPage.clickLogin();
        } else {
            throw new IllegalArgumentException("Tombol tidak dikenal: " + buttonName);
        }
    }

    @Then("sistem menolak proses login")
    public void sistem_menolak_proses_login() {
        String errorMsg = loginPage.getErrorMessage();
        assertThat(errorMsg)
                .as("Sistem harus menampilkan pesan kegagalan login")
                .isNotBlank();
    }

    @And("pengguna tetap berada di halaman login")
    public void pengguna_tetap_berada_di_halaman_login() {
        assertThat(driver.getCurrentUrl())
                .as("URL harus tetap di halaman login, bukan dashboard")
                .contains("jtk-learn")
                .doesNotContain("dashboard");
    }

    @And("sistem menampilkan notifikasi {string}")
    public void sistem_menampilkan_notifikasi(String expectedMessage) {
        String actualMessage = loginPage.getErrorMessage();
        assertThat(actualMessage)
                .as("Pesan notifikasi harus sesuai")
                .contains(expectedMessage);
    }

    @And("tidak ada pengalihan ke halaman dashboard")
    public void tidak_ada_pengalihan_ke_halaman_dashboard() {
        assertThat(driver.getCurrentUrl())
                .as("Tidak boleh ada pengalihan ke dashboard")
                .doesNotContain("dashboard");
    }

    @And("field {string} dan {string} dapat diisi ulang")
    public void field_dapat_diisi_ulang(String field1, String field2) {
        // Verifikasi field masih enabled dan dapat diinteraksikan
        loginPage.enterEmail("");
        loginPage.enterPassword("");
        // Jika tidak throw exception, field masih bisa diisi ulang
    }
}
