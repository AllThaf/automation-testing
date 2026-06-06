Feature: Course Overview Funcionality 

Scenario: Verifikasi pesan error saat tombol Daftar diklik tanpa mengisi kode pendaftaran
    Given pelajar sudah login
    And pelajar belum terdaftar pada kursus "Testing A1"
    And halaman Course Overview kursus "Testing A1" terbuka
    When pelajar membiarkan field "Kode Pendaftaran" kosong
    And pelajar menekan tombol "Daftar"
    Then sistem menampilkan pesan error "Silakan masukkan kode pendaftaran."
    And halaman tidak berpindah
    And pelajar tetap berada pada halaman Course Overview
    And pendaftaran kursus tidak diproses
    And tidak ada record baru yang ditambahkan ke tabel "COURSEPARTICIPANT"
