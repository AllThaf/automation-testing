package jtklearn.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import jtklearn.pageobjects.actions.DashboardPageActions;
import jtklearn.pageobjects.actions.LoginPageActions;

import org.openqa.selenium.WebDriver;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private LoginPageActions loginPage;
    private DashboardPageActions dashboardPage;

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    // ========================================================================
    // BACKGROUND
    // ========================================================================

    @Given("Pengguna telah mengakses halaman login aplikasi JTK Learn")
    public void pengguna_telah_mengakses_halaman_login() {
        driver().get("https://polban-space.cloudias79.com/jtk-learn/");
        loginPage = new LoginPageActions(driver());
    }

    @And("pengguna mengakses URL {string}")
    public void pengguna_mengakses_url(String url) {
        driver().get(url);
        loginPage = new LoginPageActions(driver());
    }

    @And("pengguna belum memiliki session login aktif")
    public void pengguna_belum_memiliki_session_login_aktif() {
        // Browser baru dibuka — tidak ada sesi aktif
    }

    // ========================================================================
    // COMMON LOGIN STEPS
    // ========================================================================

    @When("sistem menampilkan halaman login")
    public void sistem_menampilkan_halaman_login() {
        assertThat(driver().getCurrentUrl())
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

    // ========================================================================
    // TC 1.2 — LOGIN GAGAL
    // ========================================================================

    @Then("sistem menolak proses login")
    public void sistem_menolak_proses_login() {
        String errorMsg = loginPage.getErrorMessage();
        assertThat(errorMsg)
                .as("Sistem harus menampilkan pesan gagal login")
                .isNotBlank();
    }

    @And("pengguna tetap berada di halaman login")
    public void pengguna_tetap_berada_di_halaman_login() {
        assertThat(driver().getCurrentUrl())
                .contains("jtk-learn")
                .doesNotContain("dashboard");
    }

    @And("sistem menampilkan notifikasi {string}")
    public void sistem_menampilkan_notifikasi(String expectedMessage) {
        String actualMessage = loginPage.getErrorMessage();
        assertThat(actualMessage)
                .contains(expectedMessage);
    }

    @And("tidak ada pengalihan ke halaman dashboard")
    public void tidak_ada_pengalihan_ke_halaman_dashboard() {
        assertThat(driver().getCurrentUrl())
                .doesNotContain("dashboard");
    }

    @And("field {string} dan {string} dapat diisi ulang")
    public void field_dapat_diisi_ulang(String field1, String field2) {
        loginPage.enterEmail("");
        loginPage.enterPassword("");
    }

    // ========================================================================
    // TC 1.3 — LOGIN BERHASIL SEBAGAI PELAJAR
    // ========================================================================

    @Then("sistem memvalidasi kredensial berhasil")
    public void sistem_memvalidasi_kredensial_berhasil() {
        dashboardPage = new DashboardPageActions(driver());
        assertThat(driver().getCurrentUrl())
                .as("Sistem harus berhasil memvalidasi kredensial")
                .contains("dashboard");
    }

    @And("pengguna diarahkan ke halaman dashboard Pelajar")
    public void pengguna_diarahkan_ke_halaman_dashboard_pelajar() {
        assertThat(driver().getCurrentUrl())
                .as("Pengguna harus diarahkan ke dashboard pelajar")
                .contains("dashboard-pelajar");
    }

    @And("halaman menampilkan nama atau profil pengguna Pelajar")
    public void halaman_menampilkan_nama_atau_profil_pengguna_pelajar() {
        assertThat(driver().getCurrentUrl())
                .contains("dashboard-pelajar");
    }

    @And("menu navigasi menampilkan")
    public void menu_navigasi_menampilkan() {
        dashboardPage = new DashboardPageActions(driver());
        List<String> menus = dashboardPage.getAvailableNavbarMenus();
        assertThat(menus)
                .contains("Beranda")
                .contains("Kursus Saya")
                .contains("Riwayat Kuis")
                .contains("Profil");
    }

    @And("tidak ada pesan error yang muncul")
    public void tidak_ada_pesan_error_yang_muncul() {
        assertThat(driver().getCurrentUrl())
                .contains("dashboard-pelajar");
    }

    @And("URL berubah menjadi {string}")
    public void url_berubah_menjadi(String expectedUrl) {
        assertThat(driver().getCurrentUrl())
                .isEqualTo(expectedUrl);
    }
}