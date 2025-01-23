package com.wahyusembiring.data.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.google.firebase.auth.FirebaseUser
import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.File
import com.wahyusembiring.data.model.Image
import com.wahyusembiring.data.model.Link
import com.wahyusembiring.data.model.User


// Fungsi extension untuk mengonversi Uri menjadi objek File berdasarkan metadata media
fun Uri.toFile(context: Context): File {
    // Mengambil metadata media terkait dengan Uri
    val mediaMetadata = getMediaMetadata(context, this)
    // Mengembalikan File dengan path Uri dan nama file dari metadata
    return File(this, mediaMetadata.displayName!!)
}

// Fungsi extension untuk mengonversi Uri menjadi objek Attachment berdasarkan scheme Uri
fun Uri.toAttachment(context: Context): Attachment {
    return when (scheme) {
        "http", "https" -> {
            // Jika scheme adalah http atau https, mengembalikan objek Link
            Link(this)
        }

        "content" -> {
            // Jika scheme adalah content, mengambil metadata media
            val mediaMetadata = getMediaMetadata(context, this)
            val mimeType = mediaMetadata.mimeType ?: throw NullPointerException("Mime type is null")
            // Jika mime type dimulai dengan "image/", mengembalikan objek Image
            when {
                mimeType.startsWith("image/") -> {
                    Image(this, mediaMetadata.displayName!!)
                }
                // Jika mime type bukan image, mengembalikan objek File
                else -> {
                    File(this, mediaMetadata.displayName!!)
                }
            }
        }

        // Jika scheme tidak dikenal, melempar IllegalArgumentException
        else -> throw IllegalArgumentException("Unsupported scheme: $scheme")
    }
}


// Mendeklarasikan data class MediaMetadata untuk menyimpan informasi metadata media seperti displayName dan mimeType
private data class MediaMetadata(
    val displayName: String? = null, // Nama tampilan file
    val mimeType: String? = null,   // Tipe MIME file
)

// Fungsi untuk mengambil metadata media dari content resolver menggunakan Uri
private fun getMediaMetadata(context: Context, uri: Uri): MediaMetadata {
    return context.contentResolver.query(
        uri,
        null, // Menyertakan semua kolom
        null,
        null,
        null
    )?.use { cursor ->
        // Mendapatkan index kolom DISPLAY_NAME dan MIME_TYPE dari cursor
        val displayNameColumnIndex =
            cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
        val mimeTypeColumnIndex =
            cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)

        if (cursor.moveToFirst()) {
            // Mengambil nilai displayName dan mimeType dari cursor
            val displayName = cursor.getString(displayNameColumnIndex)
            val mimeType = cursor.getString(mimeTypeColumnIndex)

            return@use MediaMetadata(displayName, mimeType)
        } else {
            // Jika cursor kosong atau tidak ada data, melempar error
            throw Error("Unexpected null returned by cursor.moveToFirst() method")
        }
    } ?: throw Error("Unexpected null returned by contentResolver query")
}

// Fungsi extension untuk mengonversi objek FirebaseUser menjadi objek User
fun FirebaseUser.toUser(): User {
    return User(
        id = uid,                   // Menyimpan UID pengguna
        name = displayName,         // Menyimpan nama pengguna
        email = email,               // Menyimpan email pengguna
        photoUrl = photoUrl?.toString(), // Menyimpan URL foto pengguna (jika ada)
    )
}