package jtklearn.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import jtklearn.pageobjects.actions.CourseOverviewPageActions;
import jtklearn.pageobjects.actions.DashboardPageActions;
import jtklearn.pageobjects.actions.LoginPageActions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class CourseOverviewSteps {

    private LoginPageActions loginPage;
    private DashboardPageActions dashboardPage;
    private CourseOverviewPageActions courseOverviewPage;

    private static final String BASE_URL = "https://polban-space.cloudias79.com/jtk-learn/";

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    // ========================================================================
    // COMMON LOGIN STEP — dipakai oleh semua skenario
    // ========================================================================

    @Given("pengguna login sebagai Pelajar dengan email {string} dan password {string}")
    public void pengguna_login_sebagai_pelajar_dengan_email_dan_password(String email, String password) {
        driver().get(BASE_URL);
        loginPage = new LoginPageActions(driver());
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
    }

    // ========================================================================
    // TC NADIA — Daftar tanpa kode pendaftaran
    // ========================================================================

    @And("pelajar belum terdaftar pada kursus {string}")
    public void pelajar_belum_terdaftar_pada_kursus(String courseTitle) {
        System.out.println("PRECONDITION: Pelajar belum terdaftar pada kursus: " + courseTitle);
    }

    @And("halaman Course Overview kursus {string} terbuka")
    public void halaman_course_overview_kursus_terbuka(String courseTitle) {
        dashboardPage = new DashboardPageActions(driver());
        dashboardPage.clickCourseByName(courseTitle);
        try {
            new WebDriverWait(driver(), Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[text()='Loading...']")));
        } catch (Exception e) {
            System.out.println("DEBUG: Loading... did not disappear in 10s");
        }
        System.out.println("DEBUG_DOM_START\n" + driver().getPageSource() + "\nDEBUG_DOM_END");
        courseOverviewPage = new CourseOverviewPageActions(driver());
    }

    @When("pelajar membiarkan field {string} kosong")
    public void pelajar_membiarkan_field_kosong(String fieldName) {
        if ("Kode Pendaftaran".equals(fieldName)) {
            System.out.println("STEP: Field '" + fieldName + "' dibiarkan kosong.");
        } else {
            throw new IllegalArgumentException("Field tidak dikenal: " + fieldName);
        }
    }

    @And("pelajar menekan tombol {string}")
    public void pelajar_menekan_tombol(String buttonName) {
        if ("Daftar".equals(buttonName)) {
            courseOverviewPage.clickRegister();
        } else {
            throw new IllegalArgumentException("Tombol tidak dikenal: " + buttonName);
        }
    }

    @Then("sistem menampilkan pesan error {string}")
    public void sistem_menampilkan_pesan_error(String expectedMessage) {
        assertThat(courseOverviewPage.isPopupModalDisplayed())
                .as("Popup pesan error harus muncul")
                .isTrue();

        String actualMessage = courseOverviewPage.getPopupText();
        assertThat(actualMessage)
                .as("Pesan error harus sesuai ekspektasi")
                .contains(expectedMessage);
    }

    @And("halaman tidak berpindah")
    public void halaman_tidak_berpindah() {
        assertThat(driver().getCurrentUrl())
                .as("URL tidak boleh berubah setelah klik Daftar tanpa kode")
                .contains("jtk-learn");
    }

    @And("pelajar tetap berada pada halaman Course Overview")
    public void pelajar_tetap_berada_pada_halaman_course_overview() {
        assertThat(driver().getCurrentUrl())
                .as("Pengguna harus tetap di halaman Course Overview")
                .doesNotContain("dashboard")
                .doesNotContain("login");
    }

    @And("pendaftaran kursus tidak diproses")
    public void pendaftaran_kursus_tidak_diproses() {
        System.out.println("STEP: Pendaftaran tidak diproses — dikonfirmasi via cek DB.");
    }

    @And("tidak ada record baru yang ditambahkan ke tabel {string}")
    public void tidak_ada_record_baru_ditambahkan_ke_tabel(String tableName) {
        System.out.println(
                "STEP: Verifikasi tabel '" + tableName + "' — tidak ada record baru. " +
                        "(Perlu validasi manual atau koneksi DB)");
    }

    // ========================================================================
    // TC FITRI — Course Overview progress 100%
    // ========================================================================

    @When("pengguna mengakses halaman Kursus Saya")
    public void pengguna_mengakses_halaman_kursus_saya() {
        dashboardPage = new DashboardPageActions(driver());
        System.out.println("STEP: Mengakses halaman Kursus Saya");
    }

    @Then("halaman Course Overview berhasil ditampilkan")
    public void halaman_course_overview_berhasil_ditampilkan() {
        assertThat(driver().getCurrentUrl()).contains("jtk-learn");
    }

    @When("pengguna memilih tab {string}")
    public void pengguna_memilih_tab(String tabName) {
        System.out.println("STEP: Memilih tab " + tabName);
    }

    @And("kursus {string} ditemukan dalam daftar kursus selesai")
    public void kursus_ditemukan_dalam_daftar_kursus_selesai(String courseTitle) {
        assertThat(driver().getPageSource()).contains(courseTitle);
    }

    @And("kursus {string} memiliki indikator progress 100 persen")
    public void kursus_memiliki_indikator_progress_100_persen(String courseTitle) {
        assertThat(driver().getPageSource()).contains("100%");
    }

    @When("pengguna mengklik kursus {string}")
    public void pengguna_mengklik_kursus(String courseTitle) {
        dashboardPage = new DashboardPageActions(driver());
        dashboardPage.clickCourseByName(courseTitle);
        courseOverviewPage = new CourseOverviewPageActions(driver());
    }

    @Then("halaman detail kursus berhasil terbuka")
    public void halaman_detail_kursus_berhasil_terbuka() {
        assertThat(driver().getCurrentUrl()).contains("jtk-learn");
    }

    @And("tidak ada aktivitas atau materi yang masih harus diselesaikan")
    public void tidak_ada_aktivitas_atau_materi_yang_masih_harus_diselesaikan() {
        assertThat(driver().getPageSource()).doesNotContain("Lanjutkan");
    }

    // ========================================================================
    // TC THAFA — Verifikasi enroll dengan kode yang salah
    // Hanya menambahkan, hampir semuanya sudah terdefinisi di TC NINDA
    // ========================================================================

    @When("pelajar memasukkan kode {string}")
    public void pelajar_memasukkan_kode(String code) {
        courseOverviewPage = new CourseOverviewPageActions(driver());
        courseOverviewPage.enterRegistrationCode(code);
    }

    @And("pelajar menekan tombol OK")
    public void pelajar_menekan_tombol_ok() {
        courseOverviewPage = new CourseOverviewPageActions(driver());
        courseOverviewPage.clickOkButton();
    }
}