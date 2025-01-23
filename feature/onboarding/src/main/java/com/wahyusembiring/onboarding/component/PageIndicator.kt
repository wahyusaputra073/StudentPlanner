package com.wahyusembiring.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier, // Modifier untuk kustomisasi tampilan
    pageSize: Int, // Jumlah total halaman
    currentPage: Int, // Halaman yang sedang aktif
    activeColor: Color = MaterialTheme.colorScheme.secondary, // Warna indikator aktif
    inactiveColor: Color = MaterialTheme.colorScheme.secondaryContainer, // Warna indikator tidak aktif
) {
    Row(
        modifier = modifier, // Menggunakan modifier yang diberikan
        horizontalArrangement = Arrangement.SpaceBetween // Menyusun indikator dengan jarak yang merata
    ) {
        repeat(pageSize) { // Mengulang sebanyak jumlah halaman
            Spacer(modifier = Modifier.width(2.5.dp)) // Menambahkan jarak antar indikator
            Box(
                modifier = Modifier
                    .height(14.dp) // Tinggi indikator
                    .width(width = if (it == currentPage) 24.dp else 16.dp) // Lebar indikator menyesuaikan halaman aktif
                    .clip(RoundedCornerShape(10.dp)) // Memberikan bentuk bulat pada indikator
                    .background(color = if (it == currentPage) activeColor else inactiveColor) // Menentukan warna indikator aktif atau tidak
            )
            Spacer(modifier = Modifier.width(2.5.dp)) // Menambahkan jarak antar indikator
        }
    }
}
