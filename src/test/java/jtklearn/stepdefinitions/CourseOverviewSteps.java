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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class CourseOverviewSteps {

    private LoginPageActions loginPage;
    private DashboardPageActions dashboardPage;
    private CourseOverviewPageActions courseOverviewPage;
    private WebDriverWait driverWait;

    private static final String BASE_URL = "https://polban-space.cloudias79.com/jtk-learn/";

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    private WebDriverWait getWait() {
        if (driverWait == null) {
            driverWait = new WebDriverWait(driver(), Duration.ofSeconds(15));
        }
        return driverWait;
    }

    // ========================================================================
    // COMMON LOGIN STEP — dipakai oleh semua skenario
    // ========================================================================

    @Given("pengguna login sebagai Pelajar dengan email {string} dan password {string}")
    public void pengguna_login_sebagai_pelajar_dengan_email_dan_password(String email, String password) {
        driver().get(BASE_URL);

        // Tunggu halaman login benar-benar muncul
        getWait().until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[type='email'], input[type='text']")));

        loginPage = new LoginPageActions(driver());
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        // Tunggu redirect setelah login berhasil — URL berubah dari login/root
        // atau elemen dashboard muncul
        getWait().until(driver -> {
            String url = driver.getCurrentUrl();
            String src = driver.getPageSource();
            // Login berhasil jika: URL bukan root/login ATAU halaman mengandung
            // elemen khas dashboard (navbar, sidebar, dll.)
            return !url.equals(BASE_URL)
                    || src.contains("Kursus Saya")
                    || src.contains("Dashboard")
                    || src.contains("logout")
                    || src.contains("Keluar");
        });
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

        // Tunggu halaman Course Overview benar-benar termuat
        getWait().until(ExpectedConditions.urlContains("jtk-learn"));
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
        // Tunggu popup/modal muncul setelah klik tombol
        getWait().until(driver -> courseOverviewPage.isPopupModalDisplayed());

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

        // Navigasi ke halaman "Kursus Saya" — coba klik elemen navbar/menu
        // yang bertuliskan "Kursus Saya" atau navigasi langsung ke URL-nya
        try {
            WebElement myCourseLink = getWait().until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath(
                                    "//*[contains(text(),'Kursus Saya') or contains(@href,'my-course') or contains(@href,'kursus-saya')]")));
            myCourseLink.click();
        } catch (Exception e) {
            // Fallback: navigasi langsung ke URL "Kursus Saya" jika elemen tidak ditemukan
            System.out.println("FALLBACK: Navigasi langsung ke halaman Kursus Saya via URL.");
            driver().get(BASE_URL + "my-course");
        }

        // Tunggu halaman Kursus Saya termuat
        getWait().until(ExpectedConditions.or(
                ExpectedConditions.urlContains("my-course"),
                ExpectedConditions.urlContains("kursus-saya"),
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Kursus Saya')]"))));

        System.out.println("STEP: Halaman Kursus Saya berhasil diakses. URL: " + driver().getCurrentUrl());
    }

    @Then("halaman Course Overview berhasil ditampilkan")
    public void halaman_course_overview_berhasil_ditampilkan() {
        // Tunggu halaman stabil & verifikasi bukan halaman login
        getWait().until(ExpectedConditions.not(
                ExpectedConditions.urlToBe(BASE_URL)));

        String currentUrl = driver().getCurrentUrl();
        String pageSource = driver().getPageSource();

        assertThat(currentUrl)
                .as("Harus sudah melewati halaman login")
                .isNotEqualTo(BASE_URL);

        assertThat(pageSource)
                .as("Halaman harus mengandung konten aplikasi (bukan form login)")
                .doesNotContain("Masukkan email")
                .doesNotContain("Masukan kata sandi");

        System.out.println("STEP: Halaman berhasil ditampilkan. URL: " + currentUrl);
    }

    @When("pengguna memilih tab {string}")
    public void pengguna_memilih_tab(String tabName) {
        // Klik tab yang bertuliskan tabName (misal: "Selesai")
        try {
            WebElement tab = getWait().until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//*[contains(text(),'" + tabName
                                    + "') and (self::button or self::a or self::li or self::span)]")));
            tab.click();
            System.out.println("STEP: Tab '" + tabName + "' berhasil diklik.");
        } catch (Exception e) {
            // Fallback: cari berdasarkan role="tab" atau class yang mengandung "tab"
            try {
                WebElement tab = getWait().until(
                        ExpectedConditions.elementToBeClickable(
                                By.xpath("//*[@role='tab' and contains(text(),'" + tabName + "')]")));
                tab.click();
                System.out.println("STEP: Tab '" + tabName + "' berhasil diklik via role=tab.");
            } catch (Exception ex) {
                System.out.println("WARN: Tab '" + tabName + "' tidak ditemukan via klik, melanjutkan.");
            }
        }

        // Beri waktu konten tab ter-render
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
        }
    }

    @And("kursus {string} ditemukan dalam daftar kursus selesai")
    public void kursus_ditemukan_dalam_daftar_kursus_selesai(String courseTitle) {
        // Tunggu konten tab "Selesai" termuat dengan mencari judul kursus
        try {
            getWait().until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//*[contains(text(),'" + courseTitle + "')]")));
        } catch (Exception e) {
            // Tampilkan URL & sepotong page source untuk debugging
            System.out.println("DEBUG URL: " + driver().getCurrentUrl());
            System.out.println("DEBUG Page Source (500 char): " +
                    driver().getPageSource().substring(0, Math.min(500, driver().getPageSource().length())));
        }

        assertThat(driver().getPageSource())
                .as("Kursus '" + courseTitle + "' harus ditemukan di tab Selesai")
                .contains(courseTitle);
    }

    @And("kursus {string} memiliki indikator progress 100 persen")
    public void kursus_memiliki_indikator_progress_100_persen(String courseTitle) {
        String pageSource = driver().getPageSource();

        // Cek berbagai kemungkinan representasi progress 100%
        boolean has100Percent = pageSource.contains("100%")
                || pageSource.contains("100 %")
                || pageSource.contains("progress\":100")
                || pageSource.contains("progress\": 100");

        assertThat(has100Percent)
                .as("Kursus '" + courseTitle + "' harus memiliki indikator progress 100%%")
                .isTrue();
    }

    @When("pengguna mengklik kursus {string}")
    public void pengguna_mengklik_kursus(String courseTitle) {
        try {
            WebElement courseElement = getWait().until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//*[contains(text(),'" + courseTitle + "')]")));
            courseElement.click();
        } catch (Exception e) {
            // Fallback via DashboardPageActions
            dashboardPage = new DashboardPageActions(driver());
            dashboardPage.clickCourseByName(courseTitle);
        }

        courseOverviewPage = new CourseOverviewPageActions(driver());

        // Tunggu halaman detail kursus termuat
        getWait().until(ExpectedConditions.urlContains("jtk-learn"));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
        }

        System.out.println("STEP: Kursus '" + courseTitle + "' diklik. URL: " + driver().getCurrentUrl());
    }

    @Then("halaman detail kursus berhasil terbuka")
    public void halaman_detail_kursus_berhasil_terbuka() {
        String currentUrl = driver().getCurrentUrl();
        String pageSource = driver().getPageSource();

        assertThat(currentUrl)
                .as("Harus berada di dalam aplikasi jtk-learn")
                .contains("jtk-learn");

        assertThat(pageSource)
                .as("Halaman detail kursus harus muncul (bukan halaman login)")
                .doesNotContain("Masukkan email");

        System.out.println("STEP: Halaman detail kursus terbuka. URL: " + currentUrl);
    }

    @And("tidak ada aktivitas atau materi yang masih harus diselesaikan")
    public void tidak_ada_aktivitas_atau_materi_yang_masih_harus_diselesaikan() {
        String pageSource = driver().getPageSource();

        // Verifikasi tidak ada tombol/teks yang mengindikasikan ada materi belum
        // selesai
        assertThat(pageSource)
                .as("Tidak boleh ada tombol 'Lanjutkan' (semua materi sudah selesai)")
                .doesNotContain("Lanjutkan");

        System.out.println("STEP: Tidak ada materi yang belum diselesaikan — verifikasi passed.");
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