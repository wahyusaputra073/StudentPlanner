package com.wahyusembiring.habit.navigation

import androidx.compose.material3.DrawerState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.wahyusembiring.auth.login.LoginScreen
import com.wahyusembiring.auth.login.LoginScreenViewModel
import com.wahyusembiring.auth.register.RegisterScreen
import com.wahyusembiring.auth.register.RegisterScreenViewModel
import com.wahyusembiring.calendar.CalendarScreen
import com.wahyusembiring.calendar.CalendarScreenViewModel
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.exam.ExamScreen
import com.wahyusembiring.exam.ExamScreenViewModel
import com.wahyusembiring.subject.screen.main.SubjectScreen
import com.wahyusembiring.subject.screen.main.SubjectScreenViewModel
import com.wahyusembiring.homework.CreateHomeworkScreen
import com.wahyusembiring.overview.OverviewScreen
import com.wahyusembiring.homework.CreateHomeworkScreenViewModel
import com.wahyusembiring.lecture.screen.addlecture.AddLectureScreen
import com.wahyusembiring.lecture.screen.addlecture.AddLecturerScreenViewModel
import com.wahyusembiring.lecture.screen.main.LecturerScreen
import com.wahyusembiring.lecture.screen.main.LecturerScreenViewModel
import com.wahyusembiring.onboarding.OnBoardingScreen
import com.wahyusembiring.onboarding.OnBoardingScreenViewModel
import com.wahyusembiring.overview.OverviewViewModel
import com.wahyusembiring.reminder.CreateReminderScreen
import com.wahyusembiring.reminder.CreateReminderScreenViewModel
import com.wahyusembiring.settings.SettingScreen
import com.wahyusembiring.settings.SettingScreenViewModel
import com.wahyusembiring.subject.screen.create.CreateSubjectScreen
import com.wahyusembiring.subject.screen.create.CreateSubjectViewModel
import com.wahyusembiring.thesisplanner.screen.planner.ThesisPlannerScreen
import com.wahyusembiring.thesisplanner.screen.planner.ThesisPlannerScreenViewModel
import com.wahyusembiring.thesisplanner.screen.thesisselection.ThesisSelectionScreen
import com.wahyusembiring.thesisplanner.screen.thesisselection.ThesisSelectionScreenViewModel

// Fungsi untuk membuat tampilan layar kosong (blank screen) dalam navigasi
fun NavGraphBuilder.blankScreen() = composable<Screen.Blank>(content = {})


// Fungsi untuk membuat tampilan layar untuk membuat tugas (homework)
fun NavGraphBuilder.createHomeworkScreen(
    navController: NavHostController
) {
    composable<Screen.CreateHomework> {
        // Mengonversi Screen.CreateHomework menjadi route
        val route = it.toRoute<Screen.CreateHomework>()

        // Mendapatkan ViewModel untuk layar CreateHomeworkScreen
        val viewModel: CreateHomeworkScreenViewModel = hiltViewModel(
            viewModelStoreOwner = it,
            creationCallback = { factory: CreateHomeworkScreenViewModel.Factory ->
                // Membuat ViewModel dengan homeworkId
                factory.create(route.homeworkId)
            }
        )

        // Menampilkan layar CreateHomeworkScreen dengan ViewModel dan NavController
        CreateHomeworkScreen(
            viewModel = viewModel,
            navController = navController
        )
    }
}


// Fungsi untuk menambahkan layar Overview ke dalam graf navigasi
fun NavGraphBuilder.overviewScreen(
    navController: NavHostController,  // Kontroler navigasi untuk beralih antar layar
    drawerState: DrawerState  // Menyimpan status dari drawer (sidebar)
) {
    composable<Screen.Overview> {
        // Mengambil ViewModel untuk OverviewScreen menggunakan Hilt
        val overviewViewModel: OverviewViewModel = hiltViewModel(it)

        // Menampilkan OverviewScreen dengan ViewModel, drawerState, dan navController
        OverviewScreen(
            viewModel = overviewViewModel,
            drawerState = drawerState,
            navController = navController
        )
    }
}

// Fungsi untuk menambahkan layar CreateSubject ke dalam graf navigasi
fun NavGraphBuilder.createSubjectScreen(
    navController: NavHostController  // Kontroler navigasi untuk beralih antar layar
) {
    composable<Screen.CreateSubject> {
        // Mengambil subjectId dari route yang diterima
        val subjectId = it.toRoute<Screen.CreateSubject>().subjectId

        // Mengambil ViewModel untuk CreateSubjectScreen menggunakan Hilt, dengan memberikan subjectId
        val viewModel: CreateSubjectViewModel = hiltViewModel(
            viewModelStoreOwner = it,
            creationCallback = { factory: CreateSubjectViewModel.Factory ->
                // Membuat ViewModel dengan subjectId menggunakan factory
                factory.create(subjectId)
            }
        )

        // Menampilkan CreateSubjectScreen dengan ViewModel dan navController
        CreateSubjectScreen(
            viewModel = viewModel,
            navController = navController
        )
    }
}


fun NavGraphBuilder.examScreen(
    navController: NavHostController
) {
    composable<Screen.CreateExam> {
        val route = it.toRoute<Screen.CreateExam>()
        val viewModel: ExamScreenViewModel = hiltViewModel(
            viewModelStoreOwner = it,
            creationCallback = { factory: ExamScreenViewModel.Factory ->
                factory.create(route.examId)
            }
        )
        ExamScreen(
            viewModel = viewModel,
            navController = navController
        )
    }
}

// Fungsi untuk menambahkan layar CreateReminder ke dalam graf navigasi
fun NavGraphBuilder.createReminderScreen(
    navController: NavHostController  // Kontroler navigasi untuk beralih antar layar
) {
    composable<Screen.CreateReminder> {
        // Mengambil route dari Screen.CreateReminder
        val route = it.toRoute<Screen.CreateReminder>()

        // Mengambil ViewModel untuk CreateReminderScreen menggunakan Hilt, dengan memberikan reminderId
        val viewModel: CreateReminderScreenViewModel = hiltViewModel(
            viewModelStoreOwner = it,
            creationCallback = { factory: CreateReminderScreenViewModel.Factory ->
                // Membuat ViewModel dengan reminderId menggunakan factory
                factory.create(route.reminderId)
            }
        )

        // Menampilkan CreateReminderScreen dengan ViewModel dan navController
        CreateReminderScreen(
            viewModel = viewModel,
            navController = navController
        )
    }
}

// Fungsi untuk menambahkan layar Calendar ke dalam graf navigasi
fun NavGraphBuilder.calendarScreen(
    navController: NavHostController,  // Kontroler navigasi untuk beralih antar layar
    drawerState: DrawerState  // Menyimpan status dari drawer (sidebar)
) {
    composable<Screen.Calendar> {
        // Mengambil ViewModel untuk CalendarScreen menggunakan Hilt
        val viewModel: CalendarScreenViewModel = hiltViewModel(it)

        // Menampilkan CalendarScreen dengan ViewModel, navController, dan drawerState
        CalendarScreen(
            viewModel = viewModel,
            navController = navController,
            drawerState = drawerState
        )
    }
}


// Fungsi untuk menambahkan layar OnBoarding ke dalam graf navigasi
fun NavGraphBuilder.onBoardingScreen(
    navController: NavHostController  // Kontroler navigasi untuk beralih antar layar
) {
    composable<Screen.OnBoarding> {
        // Mengambil ViewModel untuk OnBoardingScreen menggunakan Hilt
        val viewModel: OnBoardingScreenViewModel = hiltViewModel(it)

        // Menampilkan OnBoardingScreen dengan ViewModel dan navController
        OnBoardingScreen(
            viewModel = viewModel,
            navController = navController
        )
    }
}

// Fungsi untuk menambahkan layar ThesisSelection ke dalam graf navigasi
fun NavGraphBuilder.thesisSelectionScreen(
    navController: NavHostController,  // Kontroler navigasi untuk beralih antar layar
    drawerState: DrawerState  // Menyimpan status dari drawer (sidebar)
) {
    composable<Screen.ThesisSelection> {
        // Mengambil ViewModel untuk ThesisSelectionScreen menggunakan Hilt
        val viewModel: ThesisSelectionScreenViewModel = hiltViewModel(it)

        // Menampilkan ThesisSelectionScreen dengan ViewModel, drawerState, dan navController
        ThesisSelectionScreen(
            viewModel = viewModel,
            drawerState = drawerState,
            navController = navController
        )
    }
}



// Fungsi untuk menambahkan layar ThesisPlanner ke dalam graf navigasi
fun NavGraphBuilder.thesisPlannerScreen(
    navController: NavHostController,  // Kontroler navigasi untuk beralih antar layar
    drawerState: DrawerState  // Menyimpan status dari drawer (sidebar)
) {
    composable<Screen.ThesisPlanner> {
        // Mengambil thesisId dari rute layar ThesisPlanner
        val thesisId = it.toRoute<Screen.ThesisPlanner>().thesisId

        // Mengambil ViewModel untuk ThesisPlannerScreen menggunakan Hilt
        val viewModel: ThesisPlannerScreenViewModel = hiltViewModel(
            viewModelStoreOwner = it,
            creationCallback = { factory: ThesisPlannerScreenViewModel.Factory ->
                // Membuat ViewModel dengan tesisId yang diberikan
                factory.create(thesisId)
            }
        )

        // Menampilkan ThesisPlannerScreen dengan ViewModel, drawerState, dan navController
        ThesisPlannerScreen(
            viewModel = viewModel,
            drawerState = drawerState,
            navController = navController
        )
    }
}

// Fungsi untuk menambahkan layar Subject ke dalam graf navigasi
fun NavGraphBuilder.subjectScreen(
    navController: NavHostController,  // Kontroler navigasi untuk beralih antar layar
    drawerState: DrawerState  // Menyimpan status dari drawer (sidebar)
) {
    composable<Screen.Subject> {
        // Mengambil ViewModel untuk SubjectScreen menggunakan Hilt
        val viewModel: SubjectScreenViewModel = hiltViewModel(it)

        // Menampilkan SubjectScreen dengan ViewModel, drawerState, dan navController
        SubjectScreen(
            viewModel = viewModel,
            navController = navController,
            drawerState = drawerState
        )
    }
}


// Fungsi untuk menambahkan layar Lecture ke dalam graf navigasi
fun NavGraphBuilder.lectureScreen(
    navController: NavHostController,  // Kontroler navigasi untuk beralih antar layar
    drawerState: DrawerState  // Menyimpan status dari drawer (sidebar)
) {
    composable<Screen.Lecture> {
        // Mengambil ViewModel untuk LecturerScreen menggunakan Hilt
        val viewModel: LecturerScreenViewModel = hiltViewModel()

        // Menampilkan LecturerScreen dengan ViewModel, navController, dan drawerState
        LecturerScreen(
            viewModel = viewModel,
            navController = navController,
            drawerState = drawerState
        )
    }
}

// Fungsi untuk menambahkan layar AddLecturer ke dalam graf navigasi
fun NavGraphBuilder.addLectureScreen(
    navController: NavHostController  // Kontroler navigasi untuk beralih antar layar
) {
    composable<Screen.AddLecturer> {
        // Mengambil lecturerId dari rute layar AddLecturer
        val lectureId = it.toRoute<Screen.AddLecturer>().lecturerId

        // Mengambil ViewModel untuk AddLecturerScreen menggunakan Hilt dan mengirimkan lectureId
        val viewModel: AddLecturerScreenViewModel = hiltViewModel(
            viewModelStoreOwner = it,
            creationCallback = { factory: AddLecturerScreenViewModel.Factory ->
                // Membuat ViewModel dengan lecturerId yang diberikan
                factory.create(lectureId)
            }
        )

        // Menampilkan AddLectureScreen dengan ViewModel dan navController
        AddLectureScreen(
            viewModel = viewModel,
            navController = navController
        )
    }
}

// Fungsi untuk menambahkan layar Login ke dalam graf navigasi
fun NavGraphBuilder.loginScreen(
    navController: NavHostController  // Kontroler navigasi untuk beralih antar layar
) {
    composable<Screen.Login> {
        // Mengambil ViewModel untuk LoginScreen menggunakan Hilt
        val viewModel: LoginScreenViewModel = hiltViewModel()

        // Menampilkan LoginScreen dengan ViewModel dan navController
        LoginScreen(
            viewModel = viewModel,
            navController = navController
        )
    }
}


// Fungsi untuk menambahkan layar Register ke dalam graf navigasi
fun NavGraphBuilder.registerScreen(
    navController: NavHostController  // Kontroler navigasi untuk beralih antar layar
) {
    composable<Screen.Register> {
        // Mengambil ViewModel untuk RegisterScreen menggunakan Hilt
        val viewModel: RegisterScreenViewModel = hiltViewModel()

        // Menampilkan RegisterScreen dengan ViewModel dan navController
        RegisterScreen(
            viewModel = viewModel,
            navController = navController
        )
    }
}

// Fungsi untuk menambahkan layar Settings ke dalam graf navigasi
fun NavGraphBuilder.settingScreen(
    navController: NavHostController,  // Kontroler navigasi untuk beralih antar layar
    drawerState: DrawerState  // Menyimpan status dari drawer (sidebar)
) {
    composable<Screen.Settings> {
        // Mengambil ViewModel untuk SettingScreen menggunakan Hilt
        val viewModel: SettingScreenViewModel = hiltViewModel()

        // Menampilkan SettingScreen dengan ViewModel, navController, dan drawerState
        SettingScreen(
            viewModel = viewModel,
            navController = navController,
            drawerState = drawerState
        )
    }
}