package com.wahyusembiring.datetime

import com.wahyusembiring.datetime.formatter.Formatter
import com.wahyusembiring.datetime.formatter.FormattingStyle
import com.wahyusembiring.datetime.formatter.java.JavaPatternSymbol.DAY_OF_WEEK
import com.wahyusembiring.datetime.formatter.java.JavaFormatter
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

class Moment private constructor(
    private val instant: Instant = Clock.System.now(), // Waktu saat ini berdasarkan sistem
    private val formatter: Formatter = JavaFormatter() // Formatter untuk format waktu
) {

    private val localDateTime: LocalDateTime =
        instant.toLocalDateTime(TimeZone.currentSystemDefault()) // Mengonversi waktu menjadi LocalDateTime berdasarkan zona waktu sistem

    val hour: Int = localDateTime.hour // Menyimpan jam dari LocalDateTime

    val minute: Int = localDateTime.minute // Menyimpan menit dari LocalDateTime

    val day: Day = Day(
        dayOfMonth = localDateTime.dayOfMonth, // Menyimpan tanggal dalam bulan
        dayOfWeek = toString(DAY_OF_WEEK) // Menyimpan nama hari dalam minggu
    )

    val epochMilliseconds: Long = instant.toEpochMilliseconds() // Waktu dalam milidetik sejak epoch (1970-01-01T00:00:00Z)

    operator fun plus(duration: Duration): Moment {
        return by(instant = instant + duration) // Menambah durasi pada waktu saat ini
    }

    operator fun minus(duration: Duration): Moment {
        return by(instant = instant - duration) // Mengurangi durasi dari waktu saat ini
    }

    fun toString(pattern: String): String {
        return formatter.format(instant, pattern) // Mengonversi waktu menjadi string berdasarkan pola format tertentu
    }

    fun toString(formattingStyle: FormattingStyle): String {
        return formatter.format(instant, formattingStyle) // Mengonversi waktu berdasarkan gaya pemformatan tertentu
    }

    override fun toString(): String {
        return instant.toString() // Mengembalikan representasi string dari `Instant`
    }

    companion object {
        fun now(): Moment {
            return Moment() // Membuat objek `Moment` dengan waktu saat ini
        }

        fun by(instant: Instant): Moment {
            return Moment(instant) // Membuat objek `Moment` berdasarkan `Instant` yang diberikan
        }

        fun fromEpochMilliseconds(epochMilliseconds: Long): Moment {
            return Moment(Instant.fromEpochMilliseconds(epochMilliseconds)) // Membuat objek `Moment` dari epoch dalam milidetik
        }
    }
}