package com.wahyusembiring.ui.component.floatingactionbutton.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.wahyusembiring.ui.theme.spacing

const val SCRIM_DIM_OPACITY = 0.4f // Menentukan tingkat opacity untuk scrim

@Composable
fun Scrim(
    isVisible: Boolean = false // Menentukan apakah scrim harus ditampilkan
) {
    // Animasi untuk perubahan opacity scrim
    val opacityAnimation by animateFloatAsState(
        label = "Scrim Opacity Animation", // Label untuk animasi
        targetValue = if (isVisible) SCRIM_DIM_OPACITY else 0f // Opacity sesuai dengan visibilitas
    )

    // Box untuk menampilkan scrim dengan animasi opacity
    Box(
        modifier = Modifier
            .offset(
                x = MaterialTheme.spacing.Medium, // Memberikan offset horizontal pada scrim
                y = MaterialTheme.spacing.Medium // Memberikan offset vertikal pada scrim
            )
            .fillMaxSize() // Membuat scrim memenuhi ukuran layar
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = opacityAnimation)) // Mengatur warna latar belakang scrim dengan animasi opacity
    )
}