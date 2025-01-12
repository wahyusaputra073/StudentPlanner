package com.wahyusembiring.calendar

sealed class CalendarScreenUIEvent { // Event yang terjadi pada UI di layar kalender
    data class OnEventCompletedStateChange( // Event perubahan status tugas atau pengingat (selesai/tidak)
        val event: Any, // Event yang terlibat
        val isChecked: Boolean // Status apakah event sudah selesai (checked)
    ) : CalendarScreenUIEvent()

    data class OnDeleteEvent( // Event untuk menghapus event
        val event: Any // Event yang akan dihapus
    ) : CalendarScreenUIEvent()

    data class OnEventClick( // Event saat event diklik
        val event: Any // Event yang diklik
    ) : CalendarScreenUIEvent()
}

sealed class CalendarScreenNavigationEvent { // Event navigasi antara layar
    data class NavigateToHomeworkDetail( // Event untuk navigasi ke detail tugas
        val homeworkId: Int // ID tugas yang akan ditampilkan
    ) : CalendarScreenNavigationEvent()

    data class NavigateToExamDetail( // Event untuk navigasi ke detail ujian
        val examkId: Int // ID ujian yang akan ditampilkan
    ) : CalendarScreenNavigationEvent()

    data class NavigateToReminderDetail( // Event untuk navigasi ke detail pengingat
        val reminderId: Int // ID pengingat yang akan ditampilkan
    ) : CalendarScreenNavigationEvent()
}