package com.wahyusembiring.subject.screen.create

sealed class MissingRequiredFieldException : Exception() {  // Kelas dasar exception yang mewarisi dari Exception
    class SubjectName : MissingRequiredFieldException()  // Exception untuk nama mata kuliah yang belum diisi
    class Room : MissingRequiredFieldException()  // Exception untuk ruang mata kuliah yang belum diisi
    class Lecture : MissingRequiredFieldException()  // Exception untuk pengajar yang belum dipilih
    class Lecture2 : MissingRequiredFieldException()  // Exception untuk pengajar yang belum dipilih
}