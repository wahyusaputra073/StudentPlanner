package com.wahyusembiring.ui.component.modalbottomsheet.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing

@Composable
fun NavigationAndActionButtonHeader(
    modifier: Modifier = Modifier, // Modifier untuk menyesuaikan tampilan bar header
    onNavigationButtonClicked: () -> Unit, // Fungsi yang dipanggil saat tombol navigasi diklik
    @DrawableRes navigationButtonIcon: Int = R.drawable.ic_close, // Ikon untuk tombol navigasi (default: ikon close)
    onActionButtonClicked: () -> Unit, // Fungsi yang dipanggil saat tombol aksi diklik
    actionButtonText: String = stringResource(R.string.save), // Teks untuk tombol aksi (default: "Save")
    navigationButtonDescription: String? = null, // Deskripsi aksesibilitas untuk tombol navigasi
    colors: CloseAndSaveHeaderColors = CloseAndSaveHeaderDefaults.colors() // Warna untuk tombol close dan save
) {
    Row(
        modifier = modifier.fillMaxWidth(), // Mengisi lebar layar
        horizontalArrangement = Arrangement.SpaceBetween, // Menyusun tombol dengan jarak antara
        verticalAlignment = Alignment.CenterVertically // Menyusun tombol secara vertikal di tengah
    ) {
        // Tombol navigasi (misalnya tombol close)
        IconButton(onClick = onNavigationButtonClicked) {
            Icon(
                painter = painterResource(id = navigationButtonIcon), // Menggunakan ikon yang diberikan
                contentDescription = navigationButtonDescription, // Deskripsi untuk aksesibilitas
                tint = colors.closeButtonColor // Warna untuk ikon close
            )
        }
        // Tombol aksi (misalnya tombol save)
        Button(
            modifier = Modifier.padding(end = MaterialTheme.spacing.Medium), // Padding di sebelah kanan
            colors = colors.saveButtonColor, // Warna untuk tombol save
            onClick = onActionButtonClicked // Fungsi yang dipanggil saat tombol save diklik
        ) {
            Text(text = actionButtonText) // Teks untuk tombol save
        }
    }
}


object CloseAndSaveHeaderDefaults {
    // Fungsi untuk menghasilkan warna default tombol Close dan Save
    @Composable
    fun colors(
        closeButtonColor: Color = LocalContentColor.current, // Warna tombol close, default mengikuti warna konten
        saveButtonContainerColor: Color = ButtonDefaults.buttonColors().containerColor, // Warna latar belakang tombol save
        saveButtonContentColor: Color = ButtonDefaults.buttonColors().contentColor, // Warna teks atau ikon pada tombol save
    ): CloseAndSaveHeaderColors {
        // Membuat objek warna tombol save menggunakan warna yang diberikan
        val materialSaveButtonColor = ButtonDefaults.buttonColors(
            containerColor = saveButtonContainerColor,
            contentColor = saveButtonContentColor
        )
        // Mengembalikan objek CloseAndSaveHeaderColors yang berisi warna tombol close dan tombol save
        return CloseAndSaveHeaderColors(
            closeButtonColor = closeButtonColor, // Menyimpan warna tombol close
            saveButtonColor = materialSaveButtonColor, // Menyimpan warna tombol save
        )
    }
}

// Data class untuk menyimpan warna tombol Close dan Save
data class CloseAndSaveHeaderColors(
    val closeButtonColor: Color, // Warna tombol close
    val saveButtonColor: ButtonColors, // Warna tombol save (termasuk kontainer dan isi)
)