package com.wahyusembiring.ui.component.popup.picker.colorpicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.wahyusembiring.ui.component.modalbottomsheet.component.NavigationAndActionButtonHeader
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.component.modalbottomsheet.component.CloseAndSaveHeaderDefaults
import com.wahyusembiring.ui.theme.HabitTheme
import com.wahyusembiring.ui.theme.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class) // Menggunakan API eksperimental dari Material3
@Composable
fun ColorPicker(
    initialColor: Color, // Warna awal yang akan ditampilkan
    onDismissRequest: () -> Unit, // Callback saat dialog ditutup
    onColorConfirmed: (color: Color) -> Unit // Callback saat warna dikonfirmasi
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true) // Mengelola state bottom sheet
    var currentSelectedColor by remember { mutableStateOf(initialColor) } // Menyimpan warna yang dipilih
    val colorPickerController = rememberColorPickerController() // Kontroler untuk color picker
    val coroutineScope = rememberCoroutineScope() // Coroutine scope untuk menjalankan tugas secara asynchronous

    ModalBottomSheet(
        sheetState = sheetState, // Menentukan state bottom sheet
        onDismissRequest = onDismissRequest // Menangani penutupan bottom sheet
    ) {
        NavigationAndActionButtonHeader(
            onNavigationButtonClicked = { // Menangani klik tombol navigasi (tutup)
                coroutineScope.launch { sheetState.hide() } // Menyembunyikan bottom sheet
                    .invokeOnCompletion { onDismissRequest() } // Memanggil onDismissRequest setelah selesai
            },
            onActionButtonClicked = { // Menangani klik tombol aksi (konfirmasi)
                onColorConfirmed(currentSelectedColor) // Mengirimkan warna yang dipilih
                coroutineScope.launch { sheetState.hide() } // Menyembunyikan bottom sheet
                    .invokeOnCompletion { onDismissRequest() } // Memanggil onDismissRequest setelah selesai
            },
            navigationButtonDescription = stringResource(R.string.close_color_picker), // Deskripsi tombol tutup
            colors = CloseAndSaveHeaderDefaults.colors(
                closeButtonColor = currentSelectedColor, // Mengatur warna tombol tutup sesuai warna yang dipilih
                saveButtonContainerColor = currentSelectedColor, // Mengatur warna tombol simpan sesuai warna yang dipilih
            )
        )
        Column(
            modifier = Modifier
                .padding(MaterialTheme.spacing.Large) // Memberikan padding di sekitar kolom
        ) {
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth() // Mengisi lebar kolom
                    .height(300.dp), // Menentukan tinggi color picker
                controller = colorPickerController, // Menggunakan kontroler untuk color picker
                initialColor = initialColor, // Menentukan warna awal untuk color picker
                onColorChanged = { currentSelectedColor = it.color } // Memperbarui warna yang dipilih saat perubahan
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium)) // Memberikan ruang di antara elemen
            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth() // Mengisi lebar kolom
                    .height(35.dp), // Menentukan tinggi slider
                controller = colorPickerController, // Menggunakan kontroler untuk slider
                initialColor = initialColor // Menentukan warna awal untuk slider
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium)) // Memberikan ruang di antara elemen
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ColorPickerModalBottomSheetPreview() {
    HabitTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ColorPicker(
                onDismissRequest = { },
                onColorConfirmed = { },
                initialColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}