Feature: Login Functionality

Background:
    Given Pengguna telah mengakses halaman login aplikasi JTK Learn
    And pengguna mengakses URL "https://polban-space.cloudias79.com/jtk-learn/"
    And pengguna belum memiliki session login aktif

@login1.2 @Negatif @Nadia
Scenario: Login gagal dengan username yang tidak terdaftar di sistem
    When sistem menampilkan halaman login
    And pengguna mengisi field "Email" dengan "evanlee@gmail.com"
    And pengguna mengisi field "Password" dengan "admin123"
    And pengguna menekan tombol "Masuk"
    Then sistem menolak proses login
    And pengguna tetap berada di halaman login
    And sistem menampilkan notifikasi "Alamat email tidak ditemukan! Masukkan alamat email yang benar!"
    And tidak ada pengalihan ke halaman dashboard
    And field "Email" dan "Password" dapat diisi ulang

@login1.3 @positif @Fitri
Scenario: Memverifikasi login berhasil sebagai Pelajar

    Given Pengguna telah mengakses halaman login aplikasi JTK Learn
    And pengguna mengakses URL "https://polban-space.cloudias79.com/jtk-learn/"
    And pengguna belum memiliki session login aktif
    When sistem menampilkan halaman login
    And pengguna mengisi field "Email" dengan "fitri.salwa.tif423@polban.ac.id"
    And pengguna mengisi field "Password" dengan "fitri123"
    And pengguna menekan tombol "Masuk"
    Then sistem memvalidasi kredensial berhasil
    And pengguna diarahkan ke halaman dashboard Pelajar
    And halaman menampilkan nama atau profil pengguna Pelajar
    And menu navigasi menampilkan
    And tidak ada pesan error yang muncul
    And URL berubah menjadi "https://polban-space.cloudias79.com/jtk-learn/dashboard-pelajar"
