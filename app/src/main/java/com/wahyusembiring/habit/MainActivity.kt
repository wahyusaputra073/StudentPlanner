package com.wahyusembiring.habit


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wahyusembiring.habit.navigation.addLectureScreen
import com.wahyusembiring.habit.navigation.blankScreen
import com.wahyusembiring.habit.navigation.calendarScreen
import com.wahyusembiring.habit.navigation.createHomeworkScreen
import com.wahyusembiring.habit.navigation.createReminderScreen
import com.wahyusembiring.habit.navigation.createSubjectScreen
import com.wahyusembiring.habit.navigation.examScreen
import com.wahyusembiring.habit.navigation.subjectScreen
import com.wahyusembiring.habit.navigation.lectureScreen
import com.wahyusembiring.habit.navigation.loginScreen
import com.wahyusembiring.habit.navigation.onBoardingScreen
import com.wahyusembiring.habit.navigation.overviewScreen
import com.wahyusembiring.habit.navigation.thesisPlannerScreen
import com.wahyusembiring.habit.navigation.thesisSelectionScreen
import com.wahyusembiring.habit.scaffold.MainScaffold
import com.wahyusembiring.ui.theme.HabitTheme
import dagger.hilt.android.AndroidEntryPoint
import com.wahyusembiring.habit.navigation.registerScreen
import com.wahyusembiring.habit.navigation.settingScreen


// Mengindikasikan bahwa kelas ini adalah entry point yang di-inject dengan Hilt
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Mendeklarasikan ViewModel yang digunakan oleh MainActivity
    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    // Variabel untuk otentikasi Firebase
    private lateinit var auth: FirebaseAuth

    // Fungsi yang dipanggil saat aktivitas dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        // Menginstal SplashScreen yang ditampilkan saat aplikasi pertama kali dijalankan
        val splashScreen = installSplashScreen()

        // Inisialisasi Firebase Authentication
        auth = Firebase.auth

        // Menjaga splash screen tetap tampil hingga aplikasi siap
        splashScreen.setKeepOnScreenCondition { !viewModel.isAppReady.value }

        // Memanggil implementasi bawaan onCreate
        super.onCreate(savedInstanceState)

        // Mengaktifkan mode Edge-to-Edge untuk UI
        enableEdgeToEdge()

        // Mengatur konten menggunakan Compose
        setContent {
            // Menggunakan tema HabitTheme untuk aplikasi
            HabitTheme {
                // Membuat NavController untuk mengelola navigasi
                val navController = rememberNavController()

                // Membuat DrawerState untuk navigasi drawer
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                // Mengamati perubahan data pengguna secara real-time
                val user by viewModel.currentUser.collectAsStateWithLifecycle(null)

                // Membuat struktur scaffold utama aplikasi
                MainScaffold(
                    mainViewModel = viewModel, // ViewModel untuk aktivitas utama
                    navController = navController, // NavController untuk navigasi
                    drawerState = drawerState, // DrawerState untuk navigasi drawer
                    user = user, // Data pengguna yang terautentikasi
                    screens = {
                        // Memanggil berbagai fungsi layar yang ditampilkan dalam aplikasi
                        blankScreen()
                        createHomeworkScreen(navController)
                        overviewScreen(navController, drawerState)
                        createSubjectScreen(navController)
                        examScreen(navController)
                        createReminderScreen(navController)
                        calendarScreen(navController, drawerState)
                        onBoardingScreen(navController)
//                        thesisSelectionScreen(navController, drawerState)
//                        thesisPlannerScreen(navController, drawerState)
                        subjectScreen(navController, drawerState)
                        lectureScreen(navController, drawerState)
                        addLectureScreen(navController)
                        loginScreen(navController)
                        registerScreen(navController)
                        settingScreen(navController, drawerState)
                    }
                )
            }
        }
    }

    // Fungsi yang dipanggil saat aktivitas dijeda (pause)
    override fun onPause() {
        viewModel.onActivityPause() // Menangani logika pause di ViewModel
        super.onPause() // Memanggil implementasi bawaan onPause
    }
}
