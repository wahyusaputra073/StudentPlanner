package com.wahyusembiring.common.navigation

import kotlinx.serialization.Serializable

@Serializable  // Menandakan bahwa class ini dapat diserialisasi menggunakan kotlinx.serialization
sealed class Screen {  // Sealed class yang mewakili berbagai jenis layar dalam aplikasi

    @Serializable
    data object Blank : Screen()  // Layar kosong (Blank)

    @Serializable
    data object Login : Screen()  // Layar login

    @Serializable
    data object Register : Screen()  // Layar registrasi

    @Serializable
    data object Overview : Screen()  // Layar overview

    @Serializable
    data class CreateHomework(val homeworkId: Int = -1) : Screen()  // Layar untuk membuat tugas (Homework)

    @Serializable
    data class CreateReminder(val reminderId: Int = -1) : Screen()  // Layar untuk membuat pengingat (Reminder)

    @Serializable
    data class CreateExam(val examId: Int = -1) : Screen()  // Layar untuk membuat ujian (Exam)

    @Serializable
    data class CreateSubject(val subjectId: Int = -1) : Screen()  // Layar untuk membuat mata pelajaran (Subject)

    @Serializable
    data object ThesisSelection : Screen()  // Layar pemilihan topik skripsi (Thesis Selection)

    @Serializable
    data class ThesisPlanner(val thesisId: Int) : Screen()  // Layar perencanaan skripsi dengan ID topik

    @Serializable
    data object Calendar : Screen()  // Layar kalender

    @Serializable
    data object OnBoarding : Screen()  // Layar onboarding

    @Serializable
    data object Subject : Screen()  // Layar mata pelajaran

    @Serializable
    data object Lecture : Screen()  // Layar perkuliahan

    @Serializable
    data class AddLecturer(val lecturerId: Int = -1) : Screen()  // Layar untuk menambah dosen

    @Serializable
    data object Settings : Screen()  // Layar pengaturan (Settings)
}
