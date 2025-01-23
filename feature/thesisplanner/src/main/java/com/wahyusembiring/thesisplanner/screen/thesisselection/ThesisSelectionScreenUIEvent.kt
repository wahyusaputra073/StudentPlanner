package com.wahyusembiring.thesisplanner.screen.thesisselection


sealed class ThesisSelectionScreenUIEvent { // Kelas sealed untuk mendefinisikan berbagai event yang terjadi di layar seleksi tesis
    data class OnCreateNewThesisClick(val onNavigateToThesisPlanner: (thesisId: Int) -> Unit) : // Event untuk membuat tesis baru dan menavigasi ke planner
        ThesisSelectionScreenUIEvent()

    data class OnDeleteThesisClick(val thesis: Thesis) : // Event untuk menghapus tesis tertentu
        ThesisSelectionScreenUIEvent()
}
