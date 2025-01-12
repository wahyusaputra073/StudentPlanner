package com.wahyusembiring.ui.component.floatingactionbutton.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.wahyusembiring.ui.theme.spacing
import kotlin.math.abs
import kotlin.math.roundToInt


private const val SUB_FAB_COLUMN_ID = "fab_column"
private const val MAIN_FAB_LAYOUT_ID = "main_fab"


@Composable
fun MultiFloatingActionButton(
    modifier: Modifier = Modifier, // Modifier untuk menyesuaikan layout
    mainFloatingActionButton: @Composable () -> Unit, // Fungsi untuk menggambar FAB utama
    subFloatingActionButton: @Composable ColumnScope.() -> Unit // Fungsi untuk menggambar FAB tambahan dalam bentuk kolom
) {
    Box(
        modifier = Modifier
            .fillMaxSize(), // Membuat Box memenuhi ukuran layar
        contentAlignment = Alignment.BottomEnd // Menyusun konten di pojok kanan bawah
    ) {
        MultiFloatingActionButtonLayout(
            modifier = modifier // Menambahkan modifier pada layout FAB
        ) {
            // Menampilkan kolom untuk FAB tambahan
            Column(
                modifier = Modifier
                    .layoutId(SUB_FAB_COLUMN_ID) // Mengatur ID layout untuk kolom FAB tambahan
            ) {
                subFloatingActionButton() // Memanggil fungsi untuk menggambar FAB tambahan
            }
            // Menampilkan FAB utama dengan padding atas
            Box(
                modifier = Modifier
                    .layoutId(MAIN_FAB_LAYOUT_ID) // Mengatur ID layout untuk FAB utama
                    .padding(top = MaterialTheme.spacing.Small) // Memberikan padding kecil pada bagian atas
            ) {
                mainFloatingActionButton() // Memanggil fungsi untuk menggambar FAB utama
            }
        }
    }
}


@Composable
private fun MultiFloatingActionButtonLayout(
    modifier: Modifier = Modifier, // Modifier untuk menyesuaikan layout
    content: @Composable () -> Unit // Konten composable yang akan dimasukkan ke dalam layout
) {
    Layout(
        modifier = modifier, // Menggunakan modifier pada layout
        content = content // Konten composable yang akan diletakkan dalam layout
    ) { measurables, constraints -> // Ukur elemen berdasarkan constraints yang diberikan
        // Mendapatkan ukuran dan posisi dari FAB tambahan (sub) dan utama (main)
        val subFabColumnMeasurable = measurables.first { it.layoutId == SUB_FAB_COLUMN_ID }
        val mainFabMeasurables = measurables.first { it.layoutId == MAIN_FAB_LAYOUT_ID }

        // Mengukur dimensi dari subFabColumn dan mainFab
        val subFabColumnPlaceable = subFabColumnMeasurable.measure(constraints)
        val mainFabPlaceables = mainFabMeasurables.measure(constraints)

        // Menentukan layout akhir berdasarkan ukuran maksimum
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {
            val subFabColumnWidth = subFabColumnPlaceable.width // Lebar dari subFAB
            val mainFabWidth = mainFabPlaceables.width // Lebar dari mainFAB
            val largestWidth = maxOf(subFabColumnWidth, mainFabWidth) // Lebar terbesar di antara keduanya
            val widthDifference = abs(subFabColumnWidth - mainFabWidth) // Selisih lebar
            val xOffset = (widthDifference / 2f).roundToInt() // Menghitung offset horizontal untuk penyusunan yang simetris

            // Menempatkan subFAB di bawah mainFAB, disesuaikan dengan lebar terbesar
            subFabColumnPlaceable.placeRelative(
                x = constraints.maxWidth - largestWidth + (if (subFabColumnWidth >= mainFabWidth) 0 else xOffset),
                y = constraints.maxHeight - (mainFabPlaceables.height + subFabColumnPlaceable.height)
            )

            // Menempatkan mainFAB di posisi bawah, dengan penyusunan horizontal yang sesuai
            mainFabPlaceables.placeRelative(
                x = constraints.maxWidth - largestWidth + (if (subFabColumnWidth > mainFabWidth) xOffset else 0),
                y = constraints.maxHeight - mainFabPlaceables.height
            )
        }
    }
}

@Composable
fun SubFloatingActionButton(
    modifier: Modifier = Modifier, // Modifier untuk menyesuaikan tampilan tombol FAB
    onClick: () -> Unit = {}, // Fungsi yang dipanggil ketika tombol FAB ditekan
    isVisible: Boolean, // Menentukan apakah tombol FAB harus ditampilkan
    icon: @Composable () -> Unit, // Konten ikon untuk FAB
) {
    // Menggunakan AnimatedVisibility untuk animasi masuk dan keluar tombol FAB
    AnimatedVisibility(
        visible = isVisible, // Mengatur visibilitas berdasarkan isVisible
        enter = fadeIn() + expandIn( // Efek animasi saat muncul: memudar dan mengembang
            spring(
                Spring.DampingRatioLowBouncy, // Efek pantulan animasi
                Spring.StiffnessMediumLow // Kekuatan animasi
            )
        ),
        exit = shrinkOut() + fadeOut() // Efek animasi saat menghilang: mengecil dan memudar
    ) {
        FloatingActionButton(
            modifier = modifier
                .scale(0.8f), // Menyesuaikan ukuran FAB (skala 0.8x)
            onClick = onClick, // Fungsi yang dipanggil saat FAB diklik
            content = icon // Menyisipkan konten ikon pada FAB
        )
    }
}
