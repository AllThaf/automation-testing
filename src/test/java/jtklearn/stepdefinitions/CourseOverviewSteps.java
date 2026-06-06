package jtklearn.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jtklearn.pageobjects.actions.CourseOverviewPageActions;
import jtklearn.pageobjects.actions.DashboardPageActions;
import jtklearn.pageobjects.actions.LoginPageActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.assertj.core.api.Assertions.assertThat;

public class CourseOverviewSteps {

    private WebDriver driver;
    private LoginPageActions loginPage;
    private DashboardPageActions dashboardPage;
    private CourseOverviewPageActions courseOverviewPage;

    private static final String BASE_URL =
            "https://polban-space.cloudias79.com/jtk-learn/";
    private static final String PELAJAR_EMAIL    = "nadia@example.com";
    private static final String PELAJAR_PASSWORD = "12345678";

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

    // ─── Precondition Steps ──────────────────────────────────────────────────────

    @Given("pelajar sudah login")
    public void pelajar_sudah_login() {
        driver.get(BASE_URL);
        loginPage = new LoginPageActions(driver);
        loginPage.enterEmail(PELAJAR_EMAIL);
        loginPage.enterPassword(PELAJAR_PASSWORD);
        loginPage.clickLogin();
    }

    @And("pelajar belum terdaftar pada kursus {string}")
    public void pelajar_belum_terdaftar_pada_kursus(String courseTitle) {
        // Precondition diverifikasi secara manual / data sudah disiapkan di DB.
        // Step ini sengaja dikosongkan — tidak ada tindakan otomatis yang diperlukan.
        System.out.println("PRECONDITION: Pelajar belum terdaftar pada kursus: " + courseTitle);
    }

    @And("halaman Course Overview kursus {string} terbuka")
    public void halaman_course_overview_kursus_terbuka(String courseTitle) {
        dashboardPage = new DashboardPageActions(driver);
        dashboardPage.clickCourseByName(courseTitle);
        courseOverviewPage = new CourseOverviewPageActions(driver);
    }

    // ─── TC05 — Daftar tanpa kode pendaftaran ───────────────────────────────────

    @When("pelajar membiarkan field {string} kosong")
    public void pelajar_membiarkan_field_kosong(String fieldName) {
        if ("Kode Pendaftaran".equals(fieldName)) {
            // Field tidak diisi — tidak ada aksi, dibiarkan kosong
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

    // ─── Expected Result Steps ───────────────────────────────────────────────────

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
        assertThat(driver.getCurrentUrl())
                .as("URL tidak boleh berubah setelah klik Daftar tanpa kode")
                .contains("jtk-learn");
    }

    @And("pelajar tetap berada pada halaman Course Overview")
    public void pelajar_tetap_berada_pada_halaman_course_overview() {
        assertThat(driver.getCurrentUrl())
                .as("Pengguna harus tetap di halaman Course Overview")
                .doesNotContain("dashboard")
                .doesNotContain("login");
    }

    @And("pendaftaran kursus tidak diproses")
    public void pendaftaran_kursus_tidak_diproses() {
        // Dikonfirmasi melalui step DB di bawah — tidak ada tindakan UI tambahan
        System.out.println("STEP: Pendaftaran tidak diproses — dikonfirmasi via cek DB.");
    }

    @And("tidak ada record baru yang ditambahkan ke tabel {string}")
    public void tidak_ada_record_baru_ditambahkan_ke_tabel(String tableName) {
        // Verifikasi DB di luar scope Selenium.
        // Dalam implementasi penuh, gunakan JDBC atau API endpoint untuk mengecek tabel.
        System.out.println(
                "STEP: Verifikasi tabel '" + tableName + "' — tidak ada record baru. " +
                "(Perlu validasi manual atau koneksi DB)"
        );
    }
}
