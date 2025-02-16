package com.wahyusembiring.calendar.util

import com.wahyusembiring.data.model.ExamWithSubject
import com.wahyusembiring.data.model.HomeworkWithSubject
import com.wahyusembiring.data.model.entity.Agenda
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun List<Any>.getEventsByDate(date: LocalDate): List<Any> { // Fungsi untuk mendapatkan event berdasarkan tanggal
    return this.filter { // Memfilter elemen di dalam list yang sesuai dengan tanggal
        val eventDate = when (it) { // Memeriksa tipe data setiap elemen dalam list
            is ExamWithSubject -> it.exam.date // Jika elemen adalah ExamWithSubject, ambil tanggal ujian
            is HomeworkWithSubject -> it.homework.dueDate // Jika elemen adalah HomeworkWithSubject, ambil tanggal batas tugas
            is Agenda -> it.date // Jika elemen adalah Reminder, ambil tanggal pengingat
            else -> throw IllegalArgumentException("Invalid event type") // Jika tipe data tidak valid, lempar exception
        }
        val eventLocalDate = // Mengkonversi tanggal dari tipe Date ke LocalDate
            Instant.ofEpochMilli(eventDate.time).atZone(ZoneId.systemDefault()).toLocalDate()
        eventLocalDate == date // Membandingkan tanggal event dengan parameter 'date'
    }
}
