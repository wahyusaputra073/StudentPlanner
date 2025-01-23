package com.wahyusembiring.task

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.common.util.scheduleReminder
import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.data.model.entity.Homework
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.data.repository.EventRepository
import com.wahyusembiring.data.repository.SubjectRepository
import com.wahyusembiring.homework.R
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import kotlin.math.absoluteValue

@HiltViewModel(assistedFactory = AddTaskScreenViewModel.Factory::class)
class AddTaskScreenViewModel @AssistedInject constructor(
    @Assisted private val homeworkId: Int = -1,
    private val eventRepository: EventRepository,
    private val subjectRepository: SubjectRepository,
    private val application: Application
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(homeworkId: Int = -1): AddTaskScreenViewModel
    }

    private var getAllSubjectJob: Job? = null

    private val _state = MutableStateFlow(AddTaskScreenUIState())
    val state = _state.asStateFlow()

    fun onUIEvent(event: AddTaskUIEvent) {
        viewModelScope.launch {
            when (event) {
                is AddTaskUIEvent.OnHomeworkTitleChanged -> onHomeworkTitleChanged(event.title)
                is AddTaskUIEvent.OnExamDescriptionChanged -> onExamDescriptionChanged(event.title)
                is AddTaskUIEvent.OnSaveHomeworkButtonClicked -> onSaveHomeworkButtonClick()
                is AddTaskUIEvent.OnPickDateButtonClicked -> onDatePickerClick()
                is AddTaskUIEvent.OnPickTimeButtonClicked -> onTimePickerClick()
                is AddTaskUIEvent.OnPickDeadlineTimeButtonClicked -> onDeadlineTimePickerClick()
                is AddTaskUIEvent.OnPickSubjectButtonClicked -> onSubjectPickerClick()
                is AddTaskUIEvent.OnPickAttachmentButtonClicked -> onAttachmentPickerClick()
                is AddTaskUIEvent.OnAttachmentPicked -> onAttachmentPicked(event.attachments)
                is AddTaskUIEvent.OnConfirmSaveHomeworkClick -> onConfirmSaveHomeworkClick()
                is AddTaskUIEvent.OnDatePicked -> onDateSelected(event.date)
                is AddTaskUIEvent.OnDismissAttachmentPicker -> onDismissAttachmentPicker()
                is AddTaskUIEvent.OnDismissDatePicker -> onDismissDatePicker()
                is AddTaskUIEvent.OnDismissHomeworkSavedDialog -> onDismissHomeworkSavedDialog()
                is AddTaskUIEvent.OnDismissSaveConfirmationDialog -> onDismissSaveConfirmationDialog()
                is AddTaskUIEvent.OnDismissSubjectPicker -> onDismissSubjectPicker()
                is AddTaskUIEvent.OnDismissTimePicker -> onDismissTimePicker()
                is AddTaskUIEvent.OnDismissDeadlineTimePicker -> onDismissDeadlineTimePicker()
                is AddTaskUIEvent.OnSubjectPicked -> onSubjectSelected(event.subject)
                is AddTaskUIEvent.OnTimePicked -> onTimeSelected(event.time)
                is AddTaskUIEvent.OnDeadlineTimePicked -> onDeadlineTimeSelected(event.times)
                is AddTaskUIEvent.OnDismissErrorDialog -> onDismissErrorDialog()
                is AddTaskUIEvent.OnDismissSavingLoading -> onDismissSavingLoading()
                is AddTaskUIEvent.OnEmailAddressChanged -> onEmailAddressChanged(event.email)
                is AddTaskUIEvent.OnSendEmailButtonClicked -> sendTaskViaEmail()
                is AddTaskUIEvent.OnDismissEmailSentDialog -> onDismissEmailSentDialog()
            }
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
                    putExtra(Intent.EXTRA_SUBJECT, "Task Details: ${_state.value.homeworkTitle}")

                    val emailBody = buildString {
                        appendLine("Task Details:")
                        appendLine("Title: ${_state.value.homeworkTitle}")
                        appendLine("Subject: ${_state.value.subject?.name}")
                        appendLine("Due Date: ${_state.value.date}")
                        appendLine("Deadline: ${_state.value.times?.let { "${it.hour}:${it.minute}" } ?: "Not set"}")
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

    private fun onDismissSavingLoading() {
        _state.update { it.copy(showSavingLoading = false) }
    }

    private fun onDismissErrorDialog() {
        _state.update { it.copy(errorMessage = null) }
    }

    private fun onDismissTimePicker() {
        _state.update { it.copy(showTimePicker = false) }
    }

    private fun onDismissDeadlineTimePicker() {
        _state.update { it.copy(showDeadlineTimePicker = false) }
    }

    private fun onDismissSubjectPicker() {
        _state.update { it.copy(showSubjectPicker = false) }
    }

    private fun onDismissSaveConfirmationDialog() {
        _state.update { it.copy(showSaveConfirmationDialog = false) }
    }

    private fun onDismissHomeworkSavedDialog() {
        _state.update { it.copy(showHomeworkSavedDialog = false) }
    }

    private fun onDismissDatePicker() {
        _state.update { it.copy(showDatePicker = false) }
    }

    private fun onDismissAttachmentPicker() {
        _state.update { it.copy(showAttachmentPicker = false) }
    }

    private fun onAttachmentPicked(attachments: List<Attachment>) {
        _state.update { it.copy(attachments = attachments) }
    }

    private suspend fun onAttachmentPickerClick() {
        _state.update { it.copy(showAttachmentPicker = true) }
    }

    private suspend fun onSubjectPickerClick() {
        _state.update { it.copy(showSubjectPicker = true) }
    }

    private suspend fun onTimePickerClick() {
        _state.update { it.copy(showTimePicker = true) }
    }

    private fun onDeadlineTimePickerClick() {
        _state.update { it.copy(showDeadlineTimePicker = true) }
    }

    private suspend fun onDatePickerClick() {
        _state.update { it.copy(showDatePicker = true) }
    }

    private suspend fun onSaveHomeworkButtonClick() {
        _state.update { it.copy(showSaveConfirmationDialog = true) }
    }

    private suspend fun onConfirmSaveHomeworkClick() {
        _state.update { it.copy(showSavingLoading = true) }
        try {
            val homework = Homework(
                id = if (homeworkId == -1) 0 else homeworkId,
                title = _state.value.homeworkTitle.ifBlank { throw MissingRequiredFieldException.Title() },
                dueDate = _state.value.date ?: throw MissingRequiredFieldException.Date(),
                subjectId = _state.value.subject?.id ?: throw MissingRequiredFieldException.Subject(),
                reminder = _state.value.time,
                deadline = _state.value.times,
                description = _state.value.description,
                attachments = _state.value.attachments,
                completed = _state.value.isCompleted
            )

            val newHomeworkId = if (homeworkId == -1) {
                eventRepository.saveHomework(homework)
            } else {
                eventRepository.updateHomework(homework)
                homeworkId
            }

            try {
                // Calculate the due date's LocalDateTime
                val dueDate = LocalDateTime.ofInstant(
                    homework.dueDate.toInstant(),
                    ZoneId.systemDefault()
                )

                // Handle reminder notification
                homework.reminder?.let { reminder ->
                    // Calculate reminder time by adjusting from due date
                    val reminderDateTime = dueDate.plusHours(reminder.hour.toLong())
                        .plusMinutes(reminder.minute.toLong())

                    scheduleReminderNotification(
                        title = "${homework.title} - Reminder",
                        reminderId = newHomeworkId.toInt(),
                        localDateTime = reminderDateTime
                    )
                }

                // Handle deadline notification
                homework.deadline?.let { deadline ->
                    val deadlineTime = LocalTime.of(deadline.hour, deadline.minute)
                    val deadlineDateTime = LocalDateTime.of(
                        LocalDate.ofInstant(homework.dueDate.toInstant(), ZoneId.systemDefault()),
                        deadlineTime
                    )

                    scheduleReminderNotification(
                        title = "${homework.title} - Deadline",
                        reminderId = (newHomeworkId.toInt() + 100000), // Offset untuk membedakan dengan reminder
                        localDateTime = deadlineDateTime
                    )
                }

                _state.update {
                    it.copy(
                        showSavingLoading = false,
                        showHomeworkSavedDialog = true
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update {
                    it.copy(
                        showSavingLoading = false,
                        showHomeworkSavedDialog = true,
                        errorMessage = UIText.StringResource(R.string.notification_scheduling_failed)
                    )
                }
            }
        } catch (e: MissingRequiredFieldException) {
            _state.update { it.copy(showSavingLoading = false) }
            val errorMessage = when (e) {
                is MissingRequiredFieldException.Title -> UIText.StringResource(R.string.homework_title_is_required)
                is MissingRequiredFieldException.Date -> UIText.StringResource(R.string.due_date_is_required)
                is MissingRequiredFieldException.Subject -> UIText.StringResource(R.string.subject_is_required)
            }
            _state.update { it.copy(errorMessage = errorMessage) }
        } catch (e: IllegalArgumentException) {
            _state.update {
                it.copy(
                    showSavingLoading = false,
                    errorMessage = UIText.DynamicString(e.message ?: "Unknown error occurred.")
                )
            }
        }
    }

    private fun scheduleReminderNotification(
        context: Context = application.applicationContext,
        title: String,
        reminderId: Int,
        localDateTime: LocalDateTime
    ) {
        try {
            scheduleReminder(
                context = context,
                localDateTime = localDateTime,
                title = title,
                reminderId = reminderId
            )
        } catch (e: Exception) {
            throw RuntimeException("Failed to schedule notification for: $title", e)
        }
    }

    private fun onHomeworkTitleChanged(title: String) {
        _state.update { it.copy(homeworkTitle = title) }
    }

    private fun onExamDescriptionChanged(description: String) {
        _state.update { it.copy(description = description) }
    }

    private fun onDateSelected(date: Date) {
        _state.update { it.copy(date = date) }
    }

    private fun onTimeSelected(time: Time) {
        _state.update { it.copy(time = time) }
    }

    private fun onDeadlineTimeSelected(time: DeadlineTime) {
        _state.update { it.copy(times = time) }
    }

    private fun onSubjectSelected(subject: Subject) {
        _state.update { it.copy(subject = subject) }
    }

    init {
        if (homeworkId != -1) {
            viewModelScope.launch {
                eventRepository.getHomeworkById(homeworkId).collect { homeworkDto ->
                    if (homeworkDto == null) return@collect
                    _state.update {
                        it.copy(
                            isEditMode = true,
                            homeworkTitle = homeworkDto.homework.title,
                            date = homeworkDto.homework.dueDate,
                            time = homeworkDto.homework.reminder,
                            times = homeworkDto.homework.deadline,
                            subject = homeworkDto.subject,
                            attachments = homeworkDto.homework.attachments,
                            isCompleted = homeworkDto.homework.completed,
                            description = homeworkDto.homework.description,
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

    override fun onCleared() {
        getAllSubjectJob?.cancel()
        getAllSubjectJob = null
        super.onCleared()
    }
}