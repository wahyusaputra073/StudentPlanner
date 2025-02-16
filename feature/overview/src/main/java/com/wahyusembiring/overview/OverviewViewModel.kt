package com.wahyusembiring.overview

import androidx.lifecycle.ViewModel
import com.wahyusembiring.common.util.launch
import com.wahyusembiring.data.model.ExamWithSubject
import com.wahyusembiring.data.model.HomeworkWithSubject
import com.wahyusembiring.data.model.entity.Agenda
import com.wahyusembiring.data.repository.EventRepository
import com.wahyusembiring.datetime.Moment
import com.wahyusembiring.datetime.formatter.FormattingStyle
import com.wahyusembiring.ui.component.eventcard.EventCard
import com.wahyusembiring.ui.component.scoredialog.ScoreDialog
import com.wahyusembiring.overview.util.inside
import com.wahyusembiring.overview.util.until
import com.wahyusembiring.ui.util.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

@HiltViewModel
class OverviewViewModel @Inject constructor( // ViewModel untuk layar Overview, menggunakan Hilt untuk dependency injection
    private val eventRepository: EventRepository, // Repository untuk mengakses data event
) : ViewModel() {

    private val _state = MutableStateFlow(OverviewScreenUIState()) // State internal menggunakan StateFlow
    val state: StateFlow<OverviewScreenUIState> = _state // State publik yang dapat diamati

    init {
        loadEvents()
    }

    private fun loadEvents() {
        launch {
            eventRepository.getAllEvent().collect { events ->
                _state.update { state ->
                    state.copy(
                        eventCards = List(12) { index ->
                            val offset = index - 5
                            val currentMoment = Moment.now() + offset.days

                            EventCard(
                                title = when (offset) {
                                    0 -> UIText.StringResource(R.string.today)
                                    1 -> UIText.StringResource(R.string.tomorrow)
                                    -1 -> UIText.StringResource(R.string.yesterday)
                                    else -> UIText.DynamicString(currentMoment.day.dayOfWeek)
                                },
                                date = when (offset) {
                                    0, 1, -1 -> UIText.DynamicString(
                                        currentMoment.toString(
                                            FormattingStyle.INDO_FULL
                                        )
                                    )
                                    else -> UIText.DynamicString(
                                        currentMoment.toString(
                                            FormattingStyle.INDO_MEDIUM
                                        )
                                    )
                                },
                                events = events inside (offset.days until (offset + 1).days)
                            )
                        }
                    )
                }
            }
        }
    }

    fun onUIEvent(event: OverviewScreenUIEvent) { // Menangani event UI dari layar Overview
        when (event) {
            is OverviewScreenUIEvent.OnEventCompletedStateChange -> launch {
                onEventCompletedStateChange(event.event, event.isCompleted)
            }
            is OverviewScreenUIEvent.OnDeleteEvent -> launch {
                onDeleteEvent(event.event)
            }
            is OverviewScreenUIEvent.OnExamScorePicked -> launch {
                onExamScorePicked(event.exam, event.score)
            }
            is OverviewScreenUIEvent.OnExamScoreDialogStateChange -> {
                onExamScoreDialogStateChange(event.scoreDialog)
            }
            is OverviewScreenUIEvent.OnMarkExamAsUndone -> launch {
                onMarkExamAsUndone(event.exam)
            }

            OverviewScreenUIEvent.OnRefresh -> {
                loadEvents()
            }
        }
    }

    private suspend fun onMarkExamAsUndone(exam: ExamWithSubject) { // Menandai ujian sebagai belum selesai
        eventRepository.updateExam(exam.exam.copy(score = null))
    }

    private suspend fun onEventCompletedStateChange(event: Any, completed: Boolean) { // Mengubah status selesai dari sebuah event
        when (event) {
            is HomeworkWithSubject -> {
                eventRepository.updateHomework(event.homework.copy(completed = completed))
            }
            is ExamWithSubject -> {
                onExamScoreDialogStateChange(
                    ScoreDialog(
                        initialScore = event.exam.score ?: 0,
                        exam = event
                    )
                )
            }
            is Agenda -> {
                eventRepository.updateReminder(event.copy(completed = completed))
            }
        }
    }

    private suspend fun onExamScorePicked(exam: ExamWithSubject, score: Int) { // Menyimpan skor ujian
        eventRepository.updateExam(exam.exam.copy(score = score))
    }

    private fun onExamScoreDialogStateChange(scoreDialogState: ScoreDialog?) { // Mengubah status dialog skor ujian
        _state.update {
            it.copy(scoreDialog = scoreDialogState)
        }
    }

    private suspend fun onDeleteEvent(event: Any) { // Menghapus event dari repository
        when (event) {
            is HomeworkWithSubject -> {
                eventRepository.deleteHomework(event.homework)
            }
            is ExamWithSubject -> {
                eventRepository.deleteExam(event.exam)
            }
            is Agenda -> {
                eventRepository.deleteReminder(event)
            }
        }
    }
}
