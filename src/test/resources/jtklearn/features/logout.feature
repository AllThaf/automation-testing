Feature: Logout Functionality

  Background:
    Given Pengguna telah mengakses halaman login

  @TC_2.0.4 @Thafa
  Scenario: Verifikasi fungsi logout untuk role pelajar
    Given Pengguna login sebagai Pelajar dengan email "thafa@example.com" dan password "thafa"
    When Pengguna menekan tombol dropwdown nama akun
    And Pengguna menekan tombol keluar
    Then Pengguna berhasil logout
    And Pengguna diarahkan ke halaman login
