package com.wahyusembiring.overview

import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.data.model.ExamWithSubject
import com.wahyusembiring.ui.component.scoredialog.ScoreDialog


sealed class OverviewScreenUIEvent { // Kelas sealed yang mendefinisikan event UI untuk layar Overview
    data class OnEventCompletedStateChange(val event: Any, val isCompleted: Boolean) : // Event saat status kelengkapan suatu event berubah
        OverviewScreenUIEvent()

    data class OnDeleteEvent(val event: Any) : // Event saat suatu event dihapus
        OverviewScreenUIEvent()

    data class OnExamScorePicked(val exam: ExamWithSubject, val score: Int) : // Event saat skor ujian dipilih
        OverviewScreenUIEvent()

    data class OnMarkExamAsUndone(val exam: ExamWithSubject) : // Event saat ujian ditandai sebagai belum selesai
        OverviewScreenUIEvent()

    data class OnExamScoreDialogStateChange(val scoreDialog: ScoreDialog?) : // Event saat status dialog skor ujian berubah
        OverviewScreenUIEvent()
}
