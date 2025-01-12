package com.wahyusembiring.homework

// Sealed class untuk menangani pengecualian (exception) yang berkaitan dengan field yang belum diisi
sealed class MissingRequiredFieldException : Exception() {

   // Exception yang terjadi jika judul tugas tidak diisi
   class Title : MissingRequiredFieldException()

   // Exception yang terjadi jika tanggal tidak diisi
   class Date : MissingRequiredFieldException()

   // Exception yang terjadi jika mata pelajaran tidak dipilih
   class Subject : MissingRequiredFieldException()
}