package com.wahyusembiring.habit.scaffold

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wahyusembiring.habit.navigation.MainNavigation
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.data.model.User
import com.wahyusembiring.habit.MainViewModel
import com.wahyusembiring.habit.util.routeSimpleClassName
import com.wahyusembiring.ui.component.navigationdrawer.DrawerItem
import com.wahyusembiring.ui.component.navigationdrawer.NavigationDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Fungsi MainScaffold adalah komponen utama untuk menampilkan struktur layout yang mencakup drawer dan konten utama aplikasi
@Composable
fun MainScaffold(
    mainViewModel: MainViewModel, // ViewModel untuk mengelola data dan logika aplikasi
    navController: NavHostController, // Controller untuk navigasi antar layar
    drawerState: DrawerState, // State untuk drawer menu
    user: User? = null, // Data pengguna (opsional, bisa null)
    screens: NavGraphBuilder.() -> Unit // Fungsi untuk mendefinisikan layar-layar dalam aplikasi
) {
    // Mendapatkan entry saat ini dalam stack navigasi
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Menentukan item drawer yang dipilih berdasarkan route navigasi saat ini
    val selectedDrawerItem: DrawerItem? = remember(navBackStackEntry) {
        DrawerItem.defaultItems.find {
            it.screen.simpleName == navBackStackEntry.routeSimpleClassName
        }
    }

    // Coroutine scope untuk menjalankan task asynchronous
    val coroutineScope = rememberCoroutineScope()

    // Menampilkan NavigationDrawer dengan item yang dipilih dan data pengguna
    NavigationDrawer(
        drawerState = drawerState,
        isGesturesEnabled = selectedDrawerItem != null, // Enable gesture hanya jika ada item yang dipilih
        selectedDrawerItem = selectedDrawerItem ?: DrawerItem.defaultItems.first(), // Default item jika tidak ada item terpilih
        imageResourceIdOrUri = user?.photoUrl, // Foto pengguna, jika ada
        username = user?.name, // Nama pengguna, jika ada
        onDrawerItemClick = { // Tindakan saat item drawer diklik
            onDrawerItemClick(it, drawerState, navController, coroutineScope)
        }
    ) {
        // Scaffold untuk menampilkan layout dasar dengan padding dan konten
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0) // Mengatur padding untuk konten
        ) {
            // Menampilkan navigasi utama dan mengatur padding sesuai dengan konten
            MainNavigation(
                navController = navController,
                scaffoldPadding = it, // Padding dari scaffold
                builder = screens, // Membangun navigasi berdasarkan screens
                mainViewModel = mainViewModel // Mengirim ViewModel untuk digunakan di layar
            )
        }
    }
}

// Fungsi yang menangani klik pada item drawer dan mengarahkan ke layar yang sesuai
private fun onDrawerItemClick(
    drawerItem: DrawerItem, // Item drawer yang diklik
    drawerState: DrawerState, // State drawer
    navController: NavHostController, // Controller navigasi
    coroutineScope: CoroutineScope // CoroutineScope untuk menjalankan task async
) {
    // Menutup drawer setelah item diklik
    coroutineScope.launch { drawerState.close() }

    // Menentukan layar yang harus dibuka berdasarkan item yang dipilih
    val screen = when (drawerItem.screen) {
        Screen.Overview::class -> Screen.Overview
        Screen.Calendar::class -> Screen.Calendar
        Screen.ThesisSelection::class -> Screen.ThesisSelection
        Screen.Subject::class -> Screen.Subject
        Screen.Lecture::class -> Screen.Lecture
        Screen.AddLecturer::class -> Screen.AddLecturer
        Screen.Settings::class -> Screen.Settings
        else -> return
    }

    // Melakukan navigasi ke layar yang sesuai dengan konfigurasi popUp
    navController.navigate(screen) {
        popUpTo(navController.graph.startDestinationId) { // Menghapus semua layar sebelumnya
            inclusive = false
        }
        launchSingleTop = true // Hanya meluncurkan layar sekali
    }
}
