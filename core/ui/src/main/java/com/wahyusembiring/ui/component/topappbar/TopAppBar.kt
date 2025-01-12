package com.wahyusembiring.ui.component.topappbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.TopAppBar
import com.wahyusembiring.ui.R


@OptIn(ExperimentalMaterial3Api::class) // Mengaktifkan fitur eksperimental Material3 API
@Composable
fun TopAppBar( // Fungsi composable untuk membuat AppBar bagian atas
   title: String, // Judul yang ditampilkan di AppBar
   onMenuClick: () -> Unit // Callback untuk aksi klik pada ikon menu
) {
   TopAppBar( // Komponen Material3 untuk AppBar
      title = {
         Text(text = title) // Menampilkan teks judul pada AppBar
      },
      navigationIcon = { // Ikon navigasi di sebelah kiri AppBar
         IconButton(onClick = onMenuClick) { // Tombol ikon dengan aksi saat diklik
            Icon(
               painter = painterResource(id = R.drawable.ic_hamburger_menu), // Ikon menu hamburger
               contentDescription = stringResource(R.string.menu) // Deskripsi konten untuk aksesibilitas
            )
         }
      },
   )
}