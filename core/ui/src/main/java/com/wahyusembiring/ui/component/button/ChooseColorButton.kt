package com.wahyusembiring.ui.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing

// Fungsi Composable untuk menampilkan tombol pemilihan warna dengan kotak warna dan teks
@Composable
fun ChooseColorButton(
   color: Color,            // Warna yang akan ditampilkan dalam tombol
   onClick: () -> Unit      // Fungsi yang dipanggil saat tombol diklik
) {
   ListItem(
      modifier = Modifier
         .clickable(onClick = onClick), // Menambahkan interaksi klik pada tombol
      colors = ListItemDefaults.colors(containerColor = Color.Transparent), // Mengatur latar belakang item agar transparan
      leadingContent = {
         // Menampilkan kotak warna di sisi kiri tombol
         Box(
            modifier = Modifier
               .size(MaterialTheme.spacing.Medium) // Ukuran kotak warna sesuai tema
               .background(
                  color = color,  // Menetapkan warna yang diterima sebagai parameter
                  shape = RoundedCornerShape(50)  // Membuat sudut bulat
               )
               .clip(RoundedCornerShape(50)) // Memastikan kotak memiliki sudut bulat
               .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50)) // Menambahkan border dengan warna utama tema
         )
      },
      headlineContent = {
         // Menampilkan teks di sisi kanan tombol
         Text(
            text = stringResource(R.string.color), // Teks untuk label tombol
            color = color,  // Menggunakan warna yang diterima sebagai parameter untuk teks
         )
      }
   )
}