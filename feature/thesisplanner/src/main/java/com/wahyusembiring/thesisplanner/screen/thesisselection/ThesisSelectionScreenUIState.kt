package com.wahyusembiring.thesisplanner.screen.thesisselection

import com.wahyusembiring.data.model.ThesisWithTask

typealias Thesis = ThesisWithTask // Mendefinisikan alias tipe Thesis yang merujuk pada ThesisWithTask

data class ThesisSelectionScreenUIState( // Data class untuk menyimpan state layar seleksi tesis
    val listOfThesis: List<Thesis> = emptyList() // Properti untuk menyimpan daftar tesis, defaultnya kosong
)