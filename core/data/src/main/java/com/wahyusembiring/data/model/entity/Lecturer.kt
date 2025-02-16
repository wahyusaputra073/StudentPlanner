package com.wahyusembiring.data.model.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wahyusembiring.data.model.OfficeHour

@Entity(  // Menandai kelas ini sebagai entitas untuk Room Database
    tableName = "lecturer"  // Nama tabel dalam database
)
data class Lecturer(  // Mendefinisikan data class untuk entitas Lecturer
    @PrimaryKey(autoGenerate = true)  // Menandai kolom id sebagai primary key yang otomatis di-generate
    val id: Int = 0,
    val photo: Uri?,  // Foto dari dosen, opsional (dapat berupa URI)

    val name: String,  // Nama dosen

    val phone: List<String>,  // Daftar nomor telepon dosen

    val email: List<String>,  // Daftar alamat email dosen

    val address: List<String>,  // Daftar alamat dosen (misalnya, alamat kantor)

    val officeHour: List<OfficeHour>,  // Daftar jam kerja dosen (misalnya, jam pertemuan)

    val website: List<String>,  // Daftar URL website terkait dosen
)
