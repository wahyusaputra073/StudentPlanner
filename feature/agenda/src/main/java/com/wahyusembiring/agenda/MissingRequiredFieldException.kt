package com.wahyusembiring.agenda

sealed class MissingRequiredFieldException : Exception() {
   // Exception untuk kasus jika title kosong
   class Title : MissingRequiredFieldException()

   // Exception untuk kasus jika date kosong
   class Date : MissingRequiredFieldException()

   // Exception untuk kasus jika time kosong
   class Time : MissingRequiredFieldException()

   // Exception untuk kasus jika range (durasi) kosong
   class Range : MissingRequiredFieldException()
}