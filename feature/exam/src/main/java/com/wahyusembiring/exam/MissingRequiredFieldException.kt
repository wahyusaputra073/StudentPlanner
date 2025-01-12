package com.wahyusembiring.exam

// Kelas sealed yang digunakan untuk menangani pengecualian ketika ada field yang diperlukan pada ujian yang kosong.
sealed class MissingRequiredFieldException : Exception() {
   // Exception untuk field "Title" yang kosong
   class Title : MissingRequiredFieldException()
   // Exception untuk field "Date" yang kosong
   class Date : MissingRequiredFieldException()
   // Exception untuk field "Time" yang kosong
   class Time : MissingRequiredFieldException()
   // Exception untuk field "Times" yang kosong (misalnya waktu deadline)
   class Times : MissingRequiredFieldException()
   // Exception untuk field "Subject" yang kosong
   class Subject : MissingRequiredFieldException()
}
