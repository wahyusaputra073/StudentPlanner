package com.wahyusembiring.exam

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.common.util.launch
import com.wahyusembiring.common.util.scheduleReminder
import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.data.model.entity.Exam
import com.wahyusembiring.data.model.entity.ExamCategory
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.data.repository.EventRepository
import com.wahyusembiring.data.repository.SubjectRepository
import com.wahyusembiring.ui.util.UIText
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

@HiltViewModel(assistedFactory = AddExamScreenViewModel.Factory::class)
class AddExamScreenViewModel @AssistedInject constructor(
    @Assisted val examId: Int = -1,
    private val eventRepository: EventRepository,
    private val subjectRepository: SubjectRepository,
    private val application: Application
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(examId: Int = -1): AddExamScreenViewModel
    }

    private val _state = MutableStateFlow(AddExamScreenUIState())
    val state = _state.asStateFlow()

    private var getAllSubjectJob: Job? = null

    fun onUIEvent(event: AddExamScreenUIEvent) {
        when (event) {
            is AddExamScreenUIEvent.OnExamNameChanged -> onExamNameChanged(event.name)
            is AddExamScreenUIEvent.OnExamDescriptionChanged -> onExamDescriptionChanged(event.name)
            is AddExamScreenUIEvent.OnExamDatePickerClick -> launch { onExamDatePickerClick() }
            is AddExamScreenUIEvent.OnExamTimePickerClick -> launch { onExamTimePickerClick() }
            is AddExamScreenUIEvent.OnExamDeadlineTimePickerClick -> launch { onExamDeadlineTimePickerClick() }
            is AddExamScreenUIEvent.OnExamSubjectPickerClick -> launch { onExamSubjectPickerClick() }
            is AddExamScreenUIEvent.OnExamAttachmentPickerClick -> launch { onExamAttachmentPickerClick() }
            is AddExamScreenUIEvent.OnExamCategoryPickerClick -> launch { onExamCategoryPickerClick() }
            is AddExamScreenUIEvent.OnSaveExamButtonClick -> launch { onSaveExamButtonClick() }
            is AddExamScreenUIEvent.OnAttachmentPicked -> onAttachmentPicked(event.attachments)
            is AddExamScreenUIEvent.OnAttachmentPickedDismiss -> onAttachmentPickedDismiss()
            is AddExamScreenUIEvent.OnCategoryPicked -> onCategoryPicked(event.category)
            is AddExamScreenUIEvent.OnCategoryPickedDismiss -> onCategoryPickedDismiss()
            is AddExamScreenUIEvent.OnDatePicked -> onDatePicked(event.date)
            is AddExamScreenUIEvent.OnDatePickedDismiss -> onDatePickedDismiss()
            is AddExamScreenUIEvent.OnErrorDialogDismiss -> onErrorDialogDismiss()
            is AddExamScreenUIEvent.OnExamSavedDialogDismiss -> onExamSavedDialogDismiss()
            is AddExamScreenUIEvent.OnSaveConfirmationDialogDismiss -> onSaveConfirmationDialogDismiss()
            is AddExamScreenUIEvent.OnSaveExamConfirmClick -> launch { onSaveExamConfirmClick() }
            is AddExamScreenUIEvent.OnSubjectPicked -> onSubjectPicked(event.subject)
            is AddExamScreenUIEvent.OnSubjectPickedDismiss -> onSubjectPickedDismiss()
            is AddExamScreenUIEvent.OnTimePicked -> onTimePicked(event.time)
            is AddExamScreenUIEvent.OnDeadlineTimePicked -> onDeadlineTimePicked(event.times)
            is AddExamScreenUIEvent.OnTimePickedDismiss -> onTimePickedDismiss()
            is AddExamScreenUIEvent.OnDeadlineTimePickedDismiss -> onDeadlineTimePickedDismiss()

            is AddExamScreenUIEvent.OnEmailAddressChanged -> onEmailAddressChanged(event.email)
            is AddExamScreenUIEvent.OnSendEmailButtonClicked -> sendTaskViaEmail()
            is AddExamScreenUIEvent.OnDismissEmailSentDialog -> onDismissEmailSentDialog()
        }
    }


    private fun onEmailAddressChanged(email: String) {
        _state.update { it.copy(emailAddress = email) }
    }

    private fun onDismissEmailSentDialog() {
        _state.update { it.copy(showEmailSentDialog = false) }
    }

    private fun sendTaskViaEmail() {
        viewModelScope.launch {
            try {
                val emailIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(_state.value.emailAddress))
                    putExtra(Intent.EXTRA_SUBJECT, "[Exam] ${_state.value.name}")

                    val emailBody = buildString {
                        appendLine("Task Details:")
                        appendLine("Title: ${_state.value.name}")
                        appendLine("Subject: ${_state.value.subject?.name}")
                        appendLine("Due Date: ${SimpleDateFormat("EEE, d MMM yyyy").format(_state.value.date)}")
                        appendLine("Deadline : ${_state.value.times?.let { "${it.hour.toString().padStart(2, '0')}:${it.minute.toString().padStart(2, '0')}" } ?: "Not set"}")
                        appendLine("Description: ${_state.value.description}")
                    }

                    putExtra(Intent.EXTRA_TEXT, emailBody)
                }

                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                application.startActivity(emailIntent)
                _state.update { it.copy(showEmailSentDialog = true) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(errorMessage = UIText.DynamicString("Failed to send email: ${e.message}"))
                }
            }
        }
    }

    private fun onTimePickedDismiss() {
        _state.update { it.copy(showTimePicker = false) }
    }

    private fun onDeadlineTimePickedDismiss() {
        _state.update { it.copy(showDeadlineTimePicker = false) }
    }

    private fun onDeadlineTimePicked(times: DeadlineTime) {
        _state.update { it.copy(times = times) }
    }

    private fun onTimePicked(time: Time) {
        _state.update { it.copy(time = time) }
    }

    private fun onSubjectPickedDismiss() {
        _state.update { it.copy(showSubjectPicker = false) }
    }

    private fun onSubjectPicked(subject: Subject) {
        _state.update { it.copy(subject = subject) }
    }

    private fun onSaveConfirmationDialogDismiss() {
        _state.update { it.copy(showSaveConfirmationDialog = false) }
    }

    private fun onExamSavedDialogDismiss() {
        _state.update { it.copy(showExamSavedDialog = false) }
    }

    private fun onErrorDialogDismiss() {
        _state.update { it.copy(errorMessage = null) }
    }

    private fun onDatePickedDismiss() {
        _state.update { it.copy(showDatePicker = false) }
    }

    private fun onDatePicked(date: Date) {
        _state.update { it.copy(date = date) }
    }

    private fun onCategoryPickedDismiss() {
        _state.update { it.copy(showCategoryPicker = false) }
    }

    private fun onCategoryPicked(category: ExamCategory) {
        _state.update { it.copy(category = category) }
    }

    private fun onAttachmentPickedDismiss() {
        _state.update { it.copy(showAttachmentPicker = false) }
    }

    private fun onAttachmentPicked(attachments: List<Attachment>) {
        _state.update { it.copy(attachments = attachments) }
    }

    init {
        if (examId != -1) {
            viewModelScope.launch {
                eventRepository.getExamById(examId).collect { examWithSubject ->
                    if (examWithSubject == null) return@collect
                    _state.update {
                        it.copy(
                            isEditMode = true,
                            name = examWithSubject.exam.title,
                            date = examWithSubject.exam.date,
                            time = examWithSubject.exam.reminder,
                            times = examWithSubject.exam.deadline,
                            subject = examWithSubject.subject,
                            category = examWithSubject.exam.category,
                            score = examWithSubject.exam.score,
                            attachments = examWithSubject.exam.attachments,
                            description = examWithSubject.exam.description
                        )
                    }
                }
            }
        }
        getAllSubjectJob = viewModelScope.launch {
            subjectRepository.getAllSubject().collect { subjects ->
                _state.update { it.copy(subjects = subjects) }
            }
        }
    }

    private suspend fun onSaveExamButtonClick() {
        _state.update { it.copy(showSaveConfirmationDialog = true) }
    }

    private suspend fun onSaveExamConfirmClick() {
        _state.update { it.copy(showSavingLoading = true) }
        try {
            val exam = Exam(
                id = if (examId != -1) examId else 0,
                title = _state.value.name.ifBlank { throw MissingRequiredFieldException.Title() },
                date = _state.value.date ?: throw MissingRequiredFieldException.Date(),
                reminder = _state.value.time ?: throw MissingRequiredFieldException.Time(),
                deadline = _state.value.times ?: throw MissingRequiredFieldException.Times(),
                subjectId = _state.value.subject?.id ?: throw MissingRequiredFieldException.Subject(),
                category = _state.value.category,
                description = _state.value.description,
                attachments = _state.value.attachments,
                score = _state.value.score
            )

            val newExamId = if (examId == -1) {
                eventRepository.saveExam(exam)
            } else {
                eventRepository.updateExam(exam)
                examId
            }

            // Calculate the exam's LocalDateTime
            val examDate = LocalDateTime.ofInstant(
                exam.date.toInstant(),
                ZoneId.systemDefault()
            )

            // Handle reminder notification
            // Perbaikan pada onSaveExamConfirmClick()
// Handle reminder notification
            exam.reminder?.let { reminder ->
                // Calculate base exam date time dan reset ke 00:00:00
                val examDate = LocalDateTime.ofInstant(
                    exam.date.toInstant(),
                    ZoneId.systemDefault()
                ).withHour(0).withMinute(0).withSecond(0)

                val durationStr = "Deadline : ${_state.value.times?.let { "${it.hour.toString().padStart(2, '0')}:${it.minute.toString().padStart(2, '0')}" } ?: "Not set"}"

                // Calculate reminder time
                val reminderDateTime = examDate.plusHours(reminder.hour.toLong())
                    .plusMinutes(reminder.minute.toLong())

                scheduleReminderNotification(
                    title = "${exam.title} - Exam reminder",
                    description  = exam.description,
                    duration = durationStr,
                    reminderId = newExamId.toInt(),
                    localDateTime = reminderDateTime,
                )
            }

// Handle deadline notification
            exam.deadline?.let { deadline ->
                val deadlineDateTime = LocalDateTime.ofInstant(
                    exam.date.toInstant(),
                    ZoneId.systemDefault()
                ).withHour(deadline.hour).withMinute(deadline.minute).withSecond(0)

                val durationStr = "Deadline : ${_state.value.times?.let { "${it.hour.toString().padStart(2, '0')}:${it.minute.toString().padStart(2, '0')}" } ?: "Not set"}"

                scheduleReminderNotification(
                    title = "${exam.title} - Exam deadline",
                    description = exam.description,
                    duration = durationStr,
                    reminderId = (newExamId.toInt() + 100000),
                    localDateTime = deadlineDateTime
                )
            }

            _state.update {
                it.copy(
                    showSavingLoading = false,
                    showExamSavedDialog = true
                )
            }

        } catch (e: MissingRequiredFieldException) {
            _state.update { it.copy(showSavingLoading = false) }
            val errorMessage = when (e) {
                is MissingRequiredFieldException.Date -> UIText.StringResource(R.string.date_cannot_be_empty)
                is MissingRequiredFieldException.Subject -> UIText.StringResource(R.string.subject_cannot_be_empty)
                is MissingRequiredFieldException.Time -> UIText.StringResource(R.string.time_cannot_be_empty)
                is MissingRequiredFieldException.Times -> UIText.StringResource(R.string.deadline_time_cannot_be_empty)
                is MissingRequiredFieldException.Title -> UIText.StringResource(R.string.exam_title_cannot_be_empty)
            }
            _state.update { it.copy(errorMessage = errorMessage) }
        }
    }

    private fun scheduleReminderNotification(
        context: Context = application.applicationContext,
        title: String,
        description : String,
        reminderId: Int,
        localDateTime: LocalDateTime,
        duration: String,
    ) {
        try {
            scheduleReminder(
                context = context,
                localDateTime = localDateTime,
                title = title,
                reminderId = reminderId,
                description = description,
                duration = duration,
            )
        } catch (e: Exception) {
            throw RuntimeException("Failed to schedule notification for: $title", e)
        }
    }

    private fun onExamNameChanged(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    private fun onExamDescriptionChanged(description: String) {
        _state.value = _state.value.copy(description = description)
    }

    private fun onExamDatePickerClick() {
        _state.update { it.copy(showDatePicker = true) }
    }

    private fun onExamTimePickerClick() {
        _state.update { it.copy(showTimePicker = true) }
    }

    private fun onExamDeadlineTimePickerClick() {
        _state.update { it.copy(showDeadlineTimePicker = true) }
    }

    private fun onExamSubjectPickerClick() {
        _state.update { it.copy(showSubjectPicker = true) }
    }

    private fun onExamCategoryPickerClick() {
        _state.update { it.copy(showCategoryPicker = true) }
    }

    private fun onExamAttachmentPickerClick() {
        _state.update { it.copy(showAttachmentPicker = true) }
    }

    override fun onCleared() {
        getAllSubjectJob?.cancel()
        getAllSubjectJob = null
        super.onCleared()
    }
}