package com.wahyusembiring.ui.component.navigationdrawer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.ui.R
import kotlin.reflect.KClass

data class DrawerItem(
    @StringRes val title: Int, // Resource ID untuk teks judul
    @DrawableRes val icon: Int? = null, // Resource ID untuk ikon (opsional)
    val screen: KClass<out Screen> = Screen.Overview::class, // Kelas tujuan yang dituju saat item dipilih
    val category: Category // Kategori item dalam drawer
) {
    // Enum untuk kategori item
    enum class Category {
        CATEGORY_1, CATEGORY_2, CATEGORY_3
    }

    companion object {
        // Default items yang ditampilkan di dalam drawer
        val defaultItems: List<DrawerItem>
        get() = listOf(
            DrawerItem(
                title = R.string.home, // Judul item "Home"
                icon = R.drawable.ic_home, // Ikon untuk item "Home"
                screen = Screen.Overview::class, // Tujuan layar untuk item "Home"
                category = Category.CATEGORY_1 // Kategori untuk item "Home"
            ),
            DrawerItem(
                title = R.string.calendar, // Judul item "Calendar"
                icon = R.drawable.ic_calendar, // Ikon untuk item "Calendar"
                screen = Screen.Calendar::class, // Tujuan layar untuk item "Calendar"
                category = Category.CATEGORY_1 // Kategori untuk item "Calendar"
            ),
//                DrawerItem(
//                    title = R.string.thesisplanner, // Judul item "Thesis Planner" (dikomentari)
//                    icon = R.drawable.ic_thesisplanner, // Ikon untuk item "Thesis Planner"
//                    screen = Screen.ThesisSelection::class, // Tujuan layar untuk item "Thesis Planner"
//                    category = Category.CATEGORY_1 // Kategori untuk item "Thesis Planner"
//                ),
            DrawerItem(
                title = R.string.subjects, // Judul item "Subjects"
                icon = R.drawable.ic_subjects, // Ikon untuk item "Subjects"
                screen = Screen.Subject::class, // Tujuan layar untuk item "Subjects"
                category = Category.CATEGORY_2 // Kategori untuk item "Subjects"
            ),
            DrawerItem(
                title = R.string.lectures, // Judul item "Lectures"
                icon = R.drawable.ic_teachers, // Ikon untuk item "Lectures"
                category = Category.CATEGORY_2, // Kategori untuk item "Lectures"
                screen = Screen.Lecture::class // Tujuan layar untuk item "Lectures"
            )
        )
    }
}
