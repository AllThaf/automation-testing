package jtklearn.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import jtklearn.context.TestContext;
import jtklearn.pageobjects.actions.DashboardPageActions;
import jtklearn.pageobjects.actions.LoginPageActions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private static final String BASE_URL =
            "https://polban-space.cloudias79.com/jtk-learn/";

    private WebDriver driver;
    private LoginPageActions loginPage;
    private DashboardPageActions dashboardPage;
    private TestContext testContext;

    @Before
    public void setUp() {

        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless=new");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        testContext = new TestContext();
        testContext.setDriver(driver);
    }

    @After
    public void tearDown() {

        testContext.reset();

        if (driver != null) {
            driver.quit();
        }
    }

    // ========================================================================
    // BACKGROUND
    // ========================================================================

    @Given("Pengguna telah mengakses halaman login aplikasi JTK Learn")
    public void pengguna_telah_mengakses_halaman_login() {

        driver.get(BASE_URL);
        loginPage = new LoginPageActions(driver);
    }

    @And("pengguna mengakses URL {string}")
    public void pengguna_mengakses_url(String url) {

        driver.get(url);
        loginPage = new LoginPageActions(driver);
    }

    @And("pengguna belum memiliki session login aktif")
    public void pengguna_belum_memiliki_session_login_aktif() {
        // browser baru
    }

    // ========================================================================
    // COMMON LOGIN STEPS
    // ========================================================================

    @When("sistem menampilkan halaman login")
    public void sistem_menampilkan_halaman_login() {

        assertThat(driver.getCurrentUrl())
                .contains("jtk-learn");
    }

    @And("pengguna mengisi field {string} dengan {string}")
    public void pengguna_mengisi_field_dengan(String fieldName, String value) {

        switch (fieldName) {

            case "Email":
                loginPage.enterEmail(value);
                testContext.setCurrentUserEmail(value);
                break;

            case "Password":
                loginPage.enterPassword(value);
                testContext.setCurrentUserPassword(value);
                break;

            default:
                throw new IllegalArgumentException(
                        "Field tidak dikenal: " + fieldName
                );
        }
    }

    @And("pengguna menekan tombol {string}")
    public void pengguna_menekan_tombol(String buttonName) {

        if ("Masuk".equals(buttonName)) {

            loginPage.clickLogin();

            // Tunggu redirect selesai
            try {
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(d ->
                                !d.getCurrentUrl().equals(BASE_URL)
                        );
            } catch (Exception e) {

                System.out.println("URL setelah login = "
                        + driver.getCurrentUrl());

                System.out.println("Kemungkinan login gagal atau redirect tidak terjadi.");
            }

        } else {

            throw new IllegalArgumentException(
                    "Tombol tidak dikenal: " + buttonName
            );
        }
    }

    // ========================================================================
    // LOGIN GAGAL
    // ========================================================================

    @Then("sistem menolak proses login")
    public void sistem_menolak_proses_login() {

        String errorMsg = loginPage.getErrorMessage();

        assertThat(errorMsg)
                .isNotBlank();
    }

    @And("pengguna tetap berada di halaman login")
    public void pengguna_tetap_berada_di_halaman_login() {

        assertThat(driver.getCurrentUrl())
                .contains("jtk-learn");
    }

    @And("sistem menampilkan notifikasi {string}")
    public void sistem_menampilkan_notifikasi(String expectedMessage) {

        String actualMessage = loginPage.getErrorMessage();

        assertThat(actualMessage)
                .contains(expectedMessage);
    }

    @And("tidak ada pengalihan ke halaman dashboard")
    public void tidak_ada_pengalihan_ke_halaman_dashboard() {

        assertThat(driver.getCurrentUrl())
                .doesNotContain("dashboard");
    }

    @And("field {string} dan {string} dapat diisi ulang")
    public void field_dapat_diisi_ulang(String field1, String field2) {

        loginPage.enterEmail("");
        loginPage.enterPassword("");
    }

    // ========================================================================
    // TC 1.3 LOGIN BERHASIL
    // ========================================================================

    @Then("sistem memvalidasi kredensial berhasil")
    public void sistem_memvalidasi_kredensial_berhasil() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(ExpectedConditions.urlContains("dashboard-pelajar"));

        dashboardPage = new DashboardPageActions(driver);

        assertThat(driver.getCurrentUrl())
                .as("Sistem harus berhasil memvalidasi kredensial")
                .contains("dashboard-pelajar");
    }

    @And("pengguna diarahkan ke halaman dashboard Pelajar")
    public void pengguna_diarahkan_ke_halaman_dashboard_pelajar() {

        assertThat(driver.getCurrentUrl())
                .doesNotContain("login");
    }

    @And("halaman menampilkan nama atau profil pengguna Pelajar")
    public void halaman_menampilkan_nama_atau_profil_pengguna_pelajar() {

        assertThat(driver.getPageSource())
                .containsIgnoringCase("salwa");
    }

    @And("menu navigasi menampilkan")
    public void menu_navigasi_menampilkan() {

        dashboardPage = new DashboardPageActions(driver);

        List<String> menus =
                dashboardPage.getAvailableNavbarMenus();

        assertThat(menus).isNotEmpty();
    }

    @And("tidak ada pesan error yang muncul")
    public void tidak_ada_pesan_error_yang_muncul() {

        String pageSource = driver.getPageSource();

        assertThat(pageSource.toLowerCase())
                .doesNotContain("email atau password salah");
    }

    @And("URL berubah menjadi {string}")
    public void url_berubah_menjadi(String expectedUrl) {

        System.out.println("Expected URL = " + expectedUrl);
        System.out.println("Actual URL   = " + driver.getCurrentUrl());

        assertThat(driver.getCurrentUrl())
                .isEqualTo(expectedUrl);
    }
}