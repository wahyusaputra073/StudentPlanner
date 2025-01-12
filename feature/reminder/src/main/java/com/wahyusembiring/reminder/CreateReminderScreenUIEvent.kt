package com.wahyusembiring.reminder

import androidx.compose.ui.graphics.Color
import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.SpanTime
import com.wahyusembiring.data.model.Time
import java.util.Date

sealed class CreateReminderScreenUIEvent {

    // Event ketika judul agenda diubah oleh pengguna
    data class OnTitleChanged(val title: String) : CreateReminderScreenUIEvent()

    // Event ketika deskripsi pengingat diubah oleh pengguna
    data class OnReminderDescriptionChanged(val title: String) : CreateReminderScreenUIEvent()

    // Event ketika tombol untuk membuka DatePicker ditekan
    data object OnDatePickerButtonClick : CreateReminderScreenUIEvent()

    // Event ketika pengguna memilih tanggal di DatePicker
    data class OnDatePicked(val date: Date) : CreateReminderScreenUIEvent()

    // Event ketika DatePicker ditutup tanpa memilih tanggal
    data object OnDatePickerDismiss : CreateReminderScreenUIEvent()

    // Event ketika tombol untuk membuka TimePicker ditekan
    data object OnTimePickerButtonClick : CreateReminderScreenUIEvent()

    // Event ketika pengguna memilih waktu di TimePicker
    data class OnTimePicked(val time: Time) : CreateReminderScreenUIEvent()

    // Event ketika TimePicker ditutup tanpa memilih waktu
    data object OnTimePickerDismiss : CreateReminderScreenUIEvent()

    // Event ketika tombol untuk membuka ColorPicker ditekan
    data object OnColorPickerButtonClick : CreateReminderScreenUIEvent()

    // Event ketika pengguna memilih warna di ColorPicker
    data class OnColorPicked(val color: Color) : CreateReminderScreenUIEvent()

    // Event ketika ColorPicker ditutup tanpa memilih warna
    data object OnColorPickerDismiss : CreateReminderScreenUIEvent()

    // Event ketika tombol untuk membuka AttachmentPicker ditekan
    data object OnAttachmentPickerButtonClick : CreateReminderScreenUIEvent()

    // Event ketika pengguna menambahkan lampiran di AttachmentPicker
    data class OnAttachmentPicked(val attachments: List<Attachment>) : CreateReminderScreenUIEvent()

    // Event ketika AttachmentPicker ditutup tanpa menambahkan lampiran
    data object OnAttachmentPickerDismiss : CreateReminderScreenUIEvent()

    // Event ketika tombol untuk menyimpan pengingat ditekan
    data object OnSaveButtonClicked : CreateReminderScreenUIEvent()

    // Event ketika konfirmasi penyimpanan pengingat diterima
    data object OnSaveReminderConfirmClick : CreateReminderScreenUIEvent()

    // Event ketika dialog konfirmasi penyimpanan ditutup
    data object OnSaveConfirmationDialogDismiss : CreateReminderScreenUIEvent()

    // Event ketika dialog notifikasi pengingat tersimpan ditutup
    data object OnReminderSavedDialogDismiss : CreateReminderScreenUIEvent()

    // Event ketika dialog error ditutup
    data object OnErrorDialogDismiss : CreateReminderScreenUIEvent()

    // Event ketika tombol untuk membuka DurationPicker ditekan
    data object OnDurationTimePicker : CreateReminderScreenUIEvent()

    // Event ketika pengguna memilih durasi waktu di DurationPicker
    data class OnDurationTimePicked(val span: SpanTime) : CreateReminderScreenUIEvent()

    // Event ketika DurationPicker ditutup tanpa memilih durasi waktu
    data object OnDurationTimePickerDismiss : CreateReminderScreenUIEvent()

}