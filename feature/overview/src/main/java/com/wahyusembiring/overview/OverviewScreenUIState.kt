package com.wahyusembiring.overview

import com.wahyusembiring.ui.component.eventcard.EventCard
import com.wahyusembiring.ui.component.scoredialog.ScoreDialog

data class OverviewScreenUIState( // Kelas data untuk menyimpan state layar Overview
    val eventCards: List<EventCard> = emptyList(), // Daftar kartu event yang akan ditampilkan, default-nya kosong
    val scoreDialog: ScoreDialog? = null // Dialog skor ujian, default-nya null jika tidak ditampilkan
)
