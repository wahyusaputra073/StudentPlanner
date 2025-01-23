package com.wahyusembiring.calendar

data class CalendarScreenUIState( // Menyimpan status UI layar kalender
    val events: List<Any> = emptyList() // Daftar event yang ditampilkan di kalender, default kosong
)
