package com.wahyusembiring.agenda

import androidx.compose.ui.graphics.Color
import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.SpanTime
import com.wahyusembiring.data.model.Time
import java.util.Date

sealed class AddAgendaScreenUIEvent {

    // Event ketika judul agenda diubah oleh pengguna
    data class OnTitleChanged(val title: String) : AddAgendaScreenUIEvent()

    // Event ketika deskripsi pengingat diubah oleh pengguna
    data class OnReminderDescriptionChanged(val title: String) : AddAgendaScreenUIEvent()

    // Event ketika tombol untuk membuka DatePicker ditekan
    data object OnDatePickerButtonClick : AddAgendaScreenUIEvent()

    // Event ketika pengguna memilih tanggal di DatePicker
    data class OnDatePicked(val date: Date) : AddAgendaScreenUIEvent()

    // Event ketika DatePicker ditutup tanpa memilih tanggal
    data object OnDatePickerDismiss : AddAgendaScreenUIEvent()

    // Event ketika tombol untuk membuka TimePicker ditekan
    data object OnTimePickerButtonClick : AddAgendaScreenUIEvent()

    // Event ketika pengguna memilih waktu di TimePicker
    data class OnTimePicked(val time: Time) : AddAgendaScreenUIEvent()

    // Event ketika TimePicker ditutup tanpa memilih waktu
    data object OnTimePickerDismiss : AddAgendaScreenUIEvent()

    // Event ketika tombol untuk membuka ColorPicker ditekan
    data object OnColorPickerButtonClick : AddAgendaScreenUIEvent()

    // Event ketika pengguna memilih warna di ColorPicker
    data class OnColorPicked(val color: Color) : AddAgendaScreenUIEvent()

    // Event ketika ColorPicker ditutup tanpa memilih warna
    data object OnColorPickerDismiss : AddAgendaScreenUIEvent()

    // Event ketika tombol untuk membuka AttachmentPicker ditekan
    data object OnAttachmentPickerButtonClick : AddAgendaScreenUIEvent()

    // Event ketika pengguna menambahkan lampiran di AttachmentPicker
    data class OnAttachmentPicked(val attachments: List<Attachment>) : AddAgendaScreenUIEvent()

    // Event ketika AttachmentPicker ditutup tanpa menambahkan lampiran
    data object OnAttachmentPickerDismiss : AddAgendaScreenUIEvent()

    // Event ketika tombol untuk menyimpan pengingat ditekan
    data object OnSaveButtonClicked : AddAgendaScreenUIEvent()

    // Event ketika konfirmasi penyimpanan pengingat diterima
    data object OnSaveReminderConfirmClick : AddAgendaScreenUIEvent()

    // Event ketika dialog konfirmasi penyimpanan ditutup
    data object OnSaveConfirmationDialogDismiss : AddAgendaScreenUIEvent()

    // Event ketika dialog notifikasi pengingat tersimpan ditutup
    data object OnReminderSavedDialogDismiss : AddAgendaScreenUIEvent()

    // Event ketika dialog error ditutup
    data object OnErrorDialogDismiss : AddAgendaScreenUIEvent()

    // Event ketika tombol untuk membuka DurationPicker ditekan
    data object OnDurationTimePicker : AddAgendaScreenUIEvent()

    // Event ketika pengguna memilih durasi waktu di DurationPicker
    data class OnDurationTimePicked(val span: SpanTime) : AddAgendaScreenUIEvent()

    // Event ketika DurationPicker ditutup tanpa memilih durasi waktu
    data object OnDurationTimePickerDismiss : AddAgendaScreenUIEvent()

}