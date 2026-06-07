Feature: Course Overview Funcionality 


@Nadia @negatif
Scenario: Verifikasi pesan error saat tombol Daftar diklik tanpa mengisi kode pendaftaran
    Given pengguna login sebagai Pelajar dengan email "nadia.rachma.tif423@polban.ac.id" dan password "nadia123"
    And pelajar belum terdaftar pada kursus "Contoh Kursus"
    And halaman Course Overview kursus "Contoh Kursus" terbuka
    When pelajar membiarkan field "Kode Pendaftaran" kosong
    And pelajar menekan tombol "Daftar"
    Then sistem menampilkan pesan error "Silakan masukkan kode pendaftaran!"
    And halaman tidak berpindah
    And pelajar tetap berada pada halaman Course Overview
    And pendaftaran kursus tidak diproses
    And tidak ada record baru yang ditambahkan ke tabel "COURSEPARTICIPANT"
@Fitri 
Scenario: TC-04 Course Overview menampilkan kursus Komdatjar dengan progress 100 persen
    Given pengguna login sebagai Pelajar dengan email "fitri.salwa.tif423@polban.ac.id" dan password "fitri123"
    When pengguna mengakses halaman Kursus Saya
    Then halaman Course Overview berhasil ditampilkan
    When pengguna memilih tab "Selesai"
    And kursus "Komdatjar" ditemukan dalam daftar kursus selesai
    And kursus "Komdatjar" memiliki indikator progress 100 persen
    When pengguna mengklik kursus "Komdatjar"
    Then halaman detail kursus berhasil terbuka
    And tidak ada aktivitas atau materi yang masih harus diselesaikan