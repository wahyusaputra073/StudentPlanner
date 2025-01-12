package com.wahyusembiring.data.model

import android.net.Uri

// Interface Attachment mendefinisikan bahwa setiap lampiran harus memiliki properti 'uri' bertipe Uri.
interface Attachment {
    val uri: Uri  // Properti uri yang menyimpan lokasi file atau link dalam bentuk Uri.
}

// Kelas Link mengimplementasikan interface Attachment dan hanya menyimpan uri dari link.
data class Link(override val uri: Uri) : Attachment  // Link hanya memiliki properti uri yang menunjukkan alamat URL.

// Kelas Image mengimplementasikan interface Attachment dan menyimpan informasi tentang gambar.
data class Image(
    override val uri: Uri,  // Properti uri yang menyimpan lokasi gambar.
    val fileName: String,  // Properti fileName yang menyimpan nama file gambar.
) : Attachment  // Kelas Image berfungsi untuk menyimpan gambar yang memiliki URL dan nama file.

// Kelas File mengimplementasikan interface Attachment dan menyimpan informasi tentang file biasa.
data class File(
    override val uri: Uri,  // Properti uri yang menyimpan lokasi file.
    val fileName: String,  // Properti fileName yang menyimpan nama file.
) : Attachment  // Kelas File berfungsi untuk menyimpan file yang memiliki URL dan nama file.
