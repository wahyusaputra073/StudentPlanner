package com.wahyusembiring.data.model.entity

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wahyusembiring.data.model.File

@Entity(
    tableName = "thesis"  // Menandakan bahwa entitas ini akan dipetakan ke tabel "thesis" dalam database Room.
)
data class Thesis(
    @PrimaryKey(autoGenerate = true)  // Kolom "id" akan menjadi primary key dan nilainya akan otomatis di-generate oleh Room.
    val id: Int = 0,  // ID unik untuk setiap tesis. Defaultnya adalah 0 dan akan di-generate secara otomatis.

    val title: String,  // Judul dari tesis.

    val articles: List<File>  // Daftar artikel yang terkait dengan tesis ini. Setiap artikel disimpan sebagai file.
)