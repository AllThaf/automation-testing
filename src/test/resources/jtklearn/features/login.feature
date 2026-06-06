feature: Login Functionality

Background:
    Given Pengguna telah mengakses halaman login aplikasi JTK Learn
    And pengguna mengakses URL "https://polban-space.cloudias79.com/jtk-learn/"
    And pengguna belum memiliki session login aktif

Scenario: Login gagal dengan username yang tidak terdaftar di sistem
    When sistem menampilkan halaman login
    And pengguna mengisi field "Email" dengan "evanlee@gmail.com"
    And pengguna mengisi field "Password" dengan "admin123"
    And pengguna menekan tombol "Masuk"
    Then sistem menolak proses login
    And pengguna tetap berada di halaman login
    And sistem menampilkan notifikasi "Username atau password salah"
    And tidak ada pengalihan ke halaman dashboard
    And field "Email" dan "Password" dapat diisi ulang