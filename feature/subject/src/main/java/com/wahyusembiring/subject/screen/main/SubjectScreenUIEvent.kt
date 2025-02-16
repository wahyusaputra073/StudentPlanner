package com.wahyusembiring.subject.screen.main

import com.wahyusembiring.data.model.entity.Exam
import com.wahyusembiring.data.model.entity.TaskThesis
import com.wahyusembiring.data.model.entity.Subject

sealed class SubjectScreenUIEvent {
    data object OnHamburgerMenuClick : SubjectScreenUIEvent()  // Event ketika menu hamburger di-klik
    data class OnExamClick(val exam: Exam) : SubjectScreenUIEvent()  // Event ketika exam di-klik
    data class OnHomeworkClick(val homework: TaskThesis) : SubjectScreenUIEvent()  // Event ketika homework di-klik
    data object OnFloatingActionButtonClick : SubjectScreenUIEvent()  // Event ketika tombol FAB di-klik
    data class OnSubjectClick(val subject: Subject) : SubjectScreenUIEvent()  // Event ketika subject di-klik
    data class OnDeleteSubjectClick(val subject: Subject) : SubjectScreenUIEvent()  // Event ketika tombol delete subject di-klik
}

sealed class SubjectScreenNavigationEvent {
    data class NavigateToSubjectDetail(val subject: Subject) : SubjectScreenNavigationEvent()  // Event untuk navigasi ke detail subject
}