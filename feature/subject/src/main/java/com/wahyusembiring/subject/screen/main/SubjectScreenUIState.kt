package com.wahyusembiring.subject.screen.main

import com.wahyusembiring.data.model.SubjectWithExamAndHomework

data class SubjectScreenUIState(
    val subjects: List<SubjectWithExamAndHomework> = emptyList()  // Menyimpan daftar subject dengan exam dan homework terkait
)
