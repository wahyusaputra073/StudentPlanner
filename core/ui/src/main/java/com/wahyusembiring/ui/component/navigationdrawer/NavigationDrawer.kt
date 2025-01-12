package com.wahyusembiring.ui.component.navigationdrawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing


@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier, // Modifier untuk menyesuaikan tampilan drawer
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed), // Menyimpan state drawer (terbuka atau tertutup)
    isGesturesEnabled: Boolean = true, // Menentukan apakah gesture untuk membuka/tutup drawer diizinkan
    selectedDrawerItem: DrawerItem, // Item drawer yang sedang dipilih
    drawerItems: List<DrawerItem> = DrawerItem.defaultItems, // Daftar item yang tampil di drawer
    onDrawerItemClick: (DrawerItem) -> Unit = {}, // Callback yang dipanggil saat item drawer dipilih
    imageResourceIdOrUri: Any? = R.drawable.app_icon, // Gambar untuk profil atau ikon aplikasi di header drawer
    username: String? = null, // Nama pengguna untuk ditampilkan di header drawer (opsional)
    content: @Composable () -> Unit, // Konten utama di dalam tampilan, selain drawer
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState, // Menetapkan state drawer (terbuka atau tertutup)
        gesturesEnabled = isGesturesEnabled, // Menentukan apakah gesture dibolehkan untuk membuka/tutup drawer
        drawerContent = {
            NavigationDrawerContent( // Konten yang ditampilkan di dalam drawer
                drawerItems = drawerItems, // Daftar item yang ditampilkan
                selectedDrawerItem = selectedDrawerItem, // Item yang sedang dipilih
                onDrawerItemClick = onDrawerItemClick, // Fungsi untuk menangani klik item drawer
                imageResourceIdOrUri = imageResourceIdOrUri, // Gambar header drawer (ikon aplikasi atau foto profil)
                username = username // Nama pengguna yang ditampilkan di header drawer
            )
        },
        content = content // Konten utama aplikasi yang ditampilkan di bawah drawer
    )
}


@Composable
private fun NavigationDrawerContent(
    drawerItems: List<DrawerItem> = emptyList(), // Daftar item drawer untuk ditampilkan
    selectedDrawerItem: DrawerItem, // Item yang sedang dipilih dalam drawer
    onDrawerItemClick: (DrawerItem) -> Unit = {}, // Callback untuk menangani klik item drawer
    imageResourceIdOrUri: Any?, // Gambar profil atau ikon yang akan ditampilkan di header drawer
    username: String? // Nama pengguna yang ditampilkan di header drawer (opsional)
) {
    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxWidth(0.8f) // Lebar drawer diatur menjadi 80% dari lebar layar
            .widthIn(min = 250.dp, max = 300.dp) // Lebar drawer antara 250dp dan 300dp
    ) {
        // Header drawer yang menampilkan gambar profil dan nama pengguna
        DrawerHeader(
            imageResourceIdOrUri = imageResourceIdOrUri,
            username = username
        )
        HorizontalDivider() // Pembatas horizontal setelah header
        // Body drawer yang menampilkan daftar item navigasi
        DrawerBody(
            drawerItems = drawerItems,
            selectedDrawerItem = selectedDrawerItem,
            onDrawerItemClick = onDrawerItemClick
        )
    }
}


@Composable
private fun DrawerHeader(
    imageResourceIdOrUri: Any?, // Parameter untuk image resource atau URI
    username: String?, // Parameter untuk nama pengguna
) {
    Row(
        modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.Large) // Padding horizontal besar
            .padding(top = MaterialTheme.spacing.Large, bottom = MaterialTheme.spacing.Medium), // Padding atas dan bawah
        verticalAlignment = Alignment.CenterVertically, // Menyusun elemen secara vertikal di tengah
        horizontalArrangement = Arrangement.Start // Menyusun elemen secara horizontal di kiri
    ) {
        AsyncImage(
            modifier = Modifier
                .size(48.dp) // Ukuran gambar 48dp
                .clip(RoundedCornerShape(100)), // Membuat gambar berbentuk bulat
            model = imageResourceIdOrUri ?: R.drawable.app_icon, // Menampilkan gambar berdasarkan URI atau resource default
            contentDescription = null, // Tidak ada deskripsi konten untuk gambar
            contentScale = ContentScale.Crop // Menyesuaikan gambar dengan pemotongan jika perlu
        )
        Spacer(modifier = Modifier.width(16.dp)) // Memberikan jarak antar elemen
        Column(
            horizontalAlignment = Alignment.Start, // Menyusun teks di kiri
            verticalArrangement = Arrangement.Center // Menyusun teks di tengah secara vertikal
        ) {
            Text(
                text = "Student Planner", // Menampilkan teks dengan judul aplikasi
                style = MaterialTheme.typography.titleLarge // Menggunakan gaya teks besar dari tema
            )
        }
    }
}


@Composable
private fun ColumnScope.DrawerBody(
    drawerItems: List<DrawerItem> = emptyList(), // Daftar item untuk drawer
    selectedDrawerItem: DrawerItem, // Item yang terpilih
    onDrawerItemClick: (DrawerItem) -> Unit = {} // Fungsi callback saat item drawer dipilih
) {
    val scrollState = rememberScrollState() // Inisialisasi state untuk scrollable

    Column(
        modifier = Modifier
            .weight(1f) // Membuat column mengambil sisa ruang
            .verticalScroll(scrollState) // Menambahkan scroll vertikal pada column
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium)) // Memberikan jarak di atas
        drawerItems.forEachIndexed { index, drawerItem -> // Looping untuk menampilkan setiap item
            NavigationDrawerItem(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.Large), // Padding horizontal untuk item
                icon = drawerItem.icon?.let { // Menampilkan icon jika ada
                    {
                        Icon(
                            painter = painterResource(id = drawerItem.icon), // Menampilkan ikon
                            contentDescription = stringResource(id = drawerItem.title) // Deskripsi ikon
                        )
                    }
                },
                label = { Text(text = stringResource(id = drawerItem.title)) }, // Menampilkan label dengan teks dari resource
                selected = selectedDrawerItem == drawerItem, // Menandai item yang dipilih
                onClick = {
                    onDrawerItemClick(drawerItem) // Menangani klik pada item
                }
            )
            if (drawerItem != drawerItems.last() && drawerItem.category != drawerItems[index + 1].category) // Cek jika kategori item berbeda
            {}
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium)) // Memberikan jarak di bawah
    }
}