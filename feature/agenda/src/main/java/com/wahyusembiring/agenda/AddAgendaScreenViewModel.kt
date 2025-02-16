package com.wahyusembiring.agenda

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.common.util.scheduleReminder
import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.SpanTime
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.data.model.entity.Agenda
import com.wahyusembiring.data.repository.EventRepository
import com.wahyusembiring.reminder.R
import com.wahyusembiring.ui.util.UIText
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
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

@HiltViewModel(assistedFactory = AddAgendaScreenViewModel.Factory::class)
class AddAgendaScreenViewModel @AssistedInject constructor(
    @Assisted val reminderId: Int = -1,
    private val eventRepository: EventRepository,
    private val application: Application
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(reminderId: Int = -1): AddAgendaScreenViewModel
    }

    private val _state = MutableStateFlow(AddAgendaScreenUIState())
    val state = _state.asStateFlow()

    fun onUIEvent(event: AddAgendaScreenUIEvent) {
        viewModelScope.launch {
            when (event) {
                is AddAgendaScreenUIEvent.OnTitleChanged -> onTitleChanged(event.title)
                is AddAgendaScreenUIEvent.OnReminderDescriptionChanged -> onReminderDescriptionChanged(event.title)
                is AddAgendaScreenUIEvent.OnDatePickerButtonClick -> onDatePickerButtonClick()
                is AddAgendaScreenUIEvent.OnTimePickerButtonClick -> onTimePickerButtonClick()
                is AddAgendaScreenUIEvent.OnTimePickerDismiss -> onTimePickerDismiss()
                is AddAgendaScreenUIEvent.OnDurationTimePicker -> onDurationTimePicker()
                is AddAgendaScreenUIEvent.OnDurationTimePicked -> onDurationTimePicked(event.span)
                is AddAgendaScreenUIEvent.OnDurationTimePickerDismiss -> onDurationTimePickerDismiss()
                is AddAgendaScreenUIEvent.OnAttachmentPicked -> onAttachmentPicked(event.attachments)
                is AddAgendaScreenUIEvent.OnAttachmentPickerDismiss -> onAttachmentPickerDismiss()
                is AddAgendaScreenUIEvent.OnColorPicked -> onColorPicked(event.color)
                is AddAgendaScreenUIEvent.OnColorPickerDismiss -> onColorPickerDismiss()
                is AddAgendaScreenUIEvent.OnDatePicked -> onDatePicked(event.date)
                is AddAgendaScreenUIEvent.OnDatePickerDismiss -> onDatePickerDismiss()
                is AddAgendaScreenUIEvent.OnErrorDialogDismiss -> onErrorDialogDismiss()
                is AddAgendaScreenUIEvent.OnReminderSavedDialogDismiss -> onReminderSavedDialogDismiss()
                is AddAgendaScreenUIEvent.OnSaveConfirmationDialogDismiss -> onSaveConfirmationDialogDismiss()
                is AddAgendaScreenUIEvent.OnSaveButtonClicked -> onSaveButtonClicked()
                is AddAgendaScreenUIEvent.OnSaveReminderConfirmClick -> onSaveReminderConfirmClick()
                is AddAgendaScreenUIEvent.OnTimePicked -> onTimePicked(event.time)
                AddAgendaScreenUIEvent.OnAttachmentPickerButtonClick -> TODO()
                AddAgendaScreenUIEvent.OnColorPickerButtonClick -> TODO()

                is AddAgendaScreenUIEvent.OnEmailAddressChanged -> onEmailAddressChanged(event.email)
                is AddAgendaScreenUIEvent.OnSendEmailButtonClicked -> sendTaskViaEmail()
                is AddAgendaScreenUIEvent.OnDismissEmailSentDialog -> onDismissEmailSentDialog()
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
                    putExtra(Intent.EXTRA_SUBJECT, "[Agenda] ${_state.value.title}")

                    val emailBody = buildString {
                        appendLine("Agenda Details:")
                        appendLine("Title: ${_state.value.title}")
                        appendLine("Due Date: ${SimpleDateFormat("EEE, d MMM yyyy").format(_state.value.date)}")
                        appendLine("From ${_state.value.spanTime?.let { "${it.startTime.hour.toString().padStart(2, '0')}:${it.startTime.minute.toString().padStart(2, '0')}" } ?: "Not set"} to ${_state.value.spanTime?.let { "${it.endTime.hour.toString().padStart(2, '0')}:${it.endTime.minute.toString().padStart(2, '0')}" } ?: ""}")
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


    private fun scheduleReminderNotification(
        context: Context = application.applicationContext,
        title: String,
        reminderId: Int,
        localDateTime: LocalDateTime,
        description: String,
        duration: String,
    ) {
        try {
            scheduleReminder(
                context = context,
                localDateTime = localDateTime,
                title = title,
                description = description,
                reminderId = reminderId,
                duration = duration,
            )
        } catch (e: Exception) {
            throw RuntimeException("Failed to schedule notification for: $title", e)
        }
    }



    private suspend fun onSaveReminderConfirmClick() {
        _state.update { it.copy(showSavingLoading = true) }
        try {
            val reminder = Agenda(
                id = if (reminderId != -1) reminderId else 0,
                title = _state.value.title.ifBlank { throw MissingRequiredFieldException.Title() },
                date = _state.value.date ?: throw MissingRequiredFieldException.Date(),
                time = _state.value.time ?: throw MissingRequiredFieldException.Time(),
                duration = _state.value.spanTime ?: throw MissingRequiredFieldException.Range(),
                color = _state.value.color,
                attachments = _state.value.attachments,
                description = _state.value.description,
                completed = _state.value.isCompleted
            )

            val savedReminderId = if (reminderId == -1) {
                eventRepository.saveReminder(reminder)
            } else {
                eventRepository.updateReminder(reminder)
                reminderId
            }

            // Calculate base event date time
            // Calculate base event date time
            val eventDateTime = LocalDateTime.ofInstant(
                reminder.date.toInstant(),
                ZoneId.systemDefault()
            ).withHour(0).withMinute(0).withSecond(0) // Reset waktu ke 00:00:00


            val durationStr = "From ${_state.value.spanTime?.let { "${it.startTime.hour.toString().padStart(2, '0')}:${it.startTime.minute.toString().padStart(2, '0')}" } ?: "Not set"} to ${_state.value.spanTime?.let { "${it.endTime.hour.toString().padStart(2, '0')}:${it.endTime.minute.toString().padStart(2, '0')}" } ?: ""}"

            // Handle main event notification
            scheduleReminderNotification(
                title = "${reminder.title} - Agenda reminder",
                description = reminder.description,
                duration = durationStr,
                reminderId = savedReminderId.toInt(),
                localDateTime = eventDateTime.plusHours(reminder.time.hour.toLong())
                    .plusMinutes(reminder.time.minute.toLong())
            )

            // Handle duration notifications
            reminder.duration.let { duration ->
                // Calculate duration start time
                val startDateTime = LocalDateTime.of(
                    LocalDate.ofInstant(reminder.date.toInstant(), ZoneId.systemDefault()),
                    LocalTime.of(duration.startTime.hour, duration.startTime.minute)
                )

                scheduleReminderNotification(
                    title = "${reminder.title} - Agenda start",
                    description = reminder.description,
                    duration = durationStr,
                    reminderId = (savedReminderId.toInt() * 100 + 1),
                    localDateTime = startDateTime
                )

                // Calculate duration end time
                val endDateTime = LocalDateTime.of(
                    LocalDate.ofInstant(reminder.date.toInstant(), ZoneId.systemDefault()),
                    LocalTime.of(duration.endTime.hour, duration.endTime.minute)
                )

                scheduleReminderNotification(
                    title = "${reminder.title} - Agenda end",
                    description = reminder.description,
                    duration = durationStr,
                    reminderId = (savedReminderId.toInt() * 100 + 2),
                    localDateTime = endDateTime
                )
            }

            _state.update {
                it.copy(
                    showSavingLoading = false,
                    showReminderSavedDialog = true
                )
            }

        } catch (e: MissingRequiredFieldException) {
            _state.update { it.copy(showSavingLoading = false) }
            val errorMessage = when (e) {
                is MissingRequiredFieldException.Title -> UIText.StringResource(R.string.title_is_required)
                is MissingRequiredFieldException.Date -> UIText.StringResource(R.string.date_is_required)
                is MissingRequiredFieldException.Time -> UIText.StringResource(R.string.time_is_required)
                is MissingRequiredFieldException.Range -> UIText.StringResource(R.string.range_is_required)
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

    private fun onTitleChanged(title: String) {
        _state.update { it.copy(title = title) }
    }

    private fun onReminderDescriptionChanged(description: String) {
        _state.update { it.copy(description = description) }
    }

    private fun onDatePickerButtonClick() {
        _state.update { it.copy(showDatePicker = true) }
    }

    private fun onTimePickerButtonClick() {
        _state.update { it.copy(showTimePicker = true) }
    }

    private fun onDurationTimePicker() {
        _state.update { it.copy(showDuraPicker = true) }
    }

    private fun onSaveButtonClicked() {
        _state.update { it.copy(showSaveConfirmationDialog = true) }
    }

    private fun onDatePicked(date: Date) {
        _state.update { it.copy(date = date) }
    }

    private fun onTimePicked(time: Time) {
        _state.update { it.copy(time = time) }
    }

    private fun onDurationTimePicked(span: SpanTime) {
        _state.update { it.copy(spanTime = span) }
    }

    private fun onColorPicked(color: Color) {
        _state.update { it.copy(color = color) }
    }

    private fun onAttachmentPicked(attachments: List<Attachment>) {
        _state.update { it.copy(attachments = attachments) }
    }

    private fun onDatePickerDismiss() {
        _state.update { it.copy(showDatePicker = false) }
    }

    private fun onTimePickerDismiss() {
        _state.update { it.copy(showTimePicker = false) }
    }

    private fun onDurationTimePickerDismiss() {
        _state.update { it.copy(showDuraPicker = false) }
    }

    private fun onColorPickerDismiss() {
        _state.update { it.copy(showColorPicker = false) }
    }

    private fun onAttachmentPickerDismiss() {
        _state.update { it.copy(showAttachmentPicker = false) }
    }

    private fun onSaveConfirmationDialogDismiss() {
        _state.update { it.copy(showSaveConfirmationDialog = false) }
    }

    private fun onReminderSavedDialogDismiss() {
        _state.update { it.copy(showReminderSavedDialog = false) }
    }

    private fun onErrorDialogDismiss() {
        _state.update { it.copy(errorMessage = null) }
    }

    init {
        if (reminderId != -1) {
            viewModelScope.launch {
                eventRepository.getReminderById(reminderId).collect { reminderDto ->
                    if (reminderDto == null) return@collect
                    _state.update {
                        it.copy(
                            isEditMode = true,
                            title = reminderDto.title,
                            date = reminderDto.date,
                            time = reminderDto.time,
                            spanTime = reminderDto.duration,
                            color = reminderDto.color,
                            attachments = reminderDto.attachments,
                            description = reminderDto.description,
                            isCompleted = reminderDto.completed
                        )
                    }
                }
            }
        }
    }
}