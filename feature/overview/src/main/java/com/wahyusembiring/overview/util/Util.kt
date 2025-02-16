package com.wahyusembiring.overview.util

import com.wahyusembiring.data.model.ExamWithSubject
import com.wahyusembiring.data.model.HomeworkWithSubject
import com.wahyusembiring.data.model.entity.Agenda
import com.wahyusembiring.datetime.Moment
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

infix fun List<Any>.inside(range: ClosedRange<Duration>): List<Any> {
    val today = Moment.now() // Mendapatkan waktu saat ini
    val startDay = (today - 1.days + range.start).epochMilliseconds // Menentukan waktu mulai dari range dengan penyesuaian
    val endDay = (today - 1.days + range.endInclusive).epochMilliseconds // Menentukan waktu akhir dari range dengan penyesuaian

    return filter { event -> // Memfilter daftar event berdasarkan waktu yang ada dalam range
        when (event) { // Mengecek tipe event
            is ExamWithSubject -> event.exam.date.time in startDay..endDay // Jika event adalah ujian, cek apakah waktu ujian dalam rentang
            is HomeworkWithSubject -> event.homework.dueDate.time in startDay..endDay // Jika event adalah tugas, cek apakah waktu deadline dalam rentang
            is Agenda -> event.date.time in startDay..endDay // Jika event adalah pengingat, cek apakah waktu pengingat dalam rentang
            else -> throw IllegalArgumentException("Invalid event type") // Jika tipe event tidak valid, lemparkan exception
        }
    }
}

infix fun Duration.until(duration: Duration): ClosedRange<Duration> = (this..duration) // Membuat rentang waktu antara dua durasi
