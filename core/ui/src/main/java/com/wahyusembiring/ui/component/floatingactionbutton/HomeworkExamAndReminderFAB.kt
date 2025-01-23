package com.wahyusembiring.ui.component.floatingactionbutton

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.component.floatingactionbutton.component.MultiFloatingActionButton
import com.wahyusembiring.ui.component.floatingactionbutton.component.Scrim
import com.wahyusembiring.ui.component.floatingactionbutton.component.SubFloatingActionButton


@Composable
fun HomeworkExamAndReminderFAB(
    isExpanded: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    onReminderFabClick: () -> Unit,
    onExamFabClick: () -> Unit,
    onHomeworkFabClick: () -> Unit
) {

    Scrim(isVisible = isExpanded)
    MultiFloatingActionButton(
        mainFloatingActionButton = {
            MainFloatingActionButton(
                onClick = onClick,
                isExpanded = isExpanded
            )
        },
        subFloatingActionButton = {
            SubFloatingActionButton(
                isExpanded = isExpanded,
                onReminderFabClick = {
                    onDismiss()
                    onReminderFabClick()
                },
                onExamFabClick = {
                    onDismiss()
                    onExamFabClick()
                },
                onHomeworkFabClick = {
                    onDismiss()
                    onHomeworkFabClick()
                }
            )
        }
    )
}

@Composable
private fun MainFloatingActionButton(
    modifier: Modifier = Modifier, // Modifier untuk menyesuaikan tampilan FAB
    onClick: () -> Unit, // Fungsi yang dipanggil saat FAB diklik
    isExpanded: Boolean // Menentukan apakah FAB sedang diperbesar atau tidak
) {
    FloatingActionButton(
        modifier = modifier, // Menggunakan modifier yang diberikan
        onClick = onClick // Fungsi yang dipanggil ketika FAB diklik
    ) {
        // Animasi rotasi ikon saat FAB diperbesar atau diperkecil
        val animatedDegree by animateFloatAsState(
            label = "Icon Rotation Animation", // Label untuk animasi rotasi
            targetValue = if (isExpanded) 135f else 0f, // Nilai rotasi, 135 derajat saat diperbesar, 0 derajat saat tidak
        )

        // Menampilkan ikon dengan rotasi yang diubah berdasarkan isExpanded
        Icon(
            modifier = Modifier.rotate(animatedDegree), // Mengaplikasikan rotasi pada ikon
            painter = painterResource(id = R.drawable.ic_add), // Menampilkan ikon tambah
            contentDescription = stringResource(R.string.create_task) // Deskripsi untuk aksesibilitas
        )
    }
}

@Composable
private fun ColumnScope.SubFloatingActionButton(
    modifier: Modifier = Modifier, // Modifier untuk menyesuaikan tampilan FAB
    isExpanded: Boolean, // Menentukan apakah FAB harus ditampilkan atau tidak
    onReminderFabClick: () -> Unit, // Fungsi yang dipanggil saat FAB reminder diklik
    onExamFabClick: () -> Unit, // Fungsi yang dipanggil saat FAB exam diklik
    onHomeworkFabClick: () -> Unit // Fungsi yang dipanggil saat FAB homework diklik
) {

    // Menampilkan FAB untuk reminder, jika isExpanded bernilai true
    SubFloatingActionButton(
        isVisible = isExpanded,
        onClick = onReminderFabClick,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_reminder), // Menampilkan ikon reminder
            contentDescription = stringResource(R.string.add_reminder) // Deskripsi untuk aksesibilitas
        )
    }

    // Menampilkan FAB untuk exam, jika isExpanded bernilai true
    SubFloatingActionButton(
        isVisible = isExpanded,
        onClick = onExamFabClick,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_exam), // Menampilkan ikon exam
            contentDescription = stringResource(R.string.add_exam_schedule) // Deskripsi untuk aksesibilitas
        )
    }

    // Menampilkan FAB untuk homework, jika isExpanded bernilai true
    SubFloatingActionButton(
        isVisible = isExpanded,
        onClick = onHomeworkFabClick,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_homework), // Menampilkan ikon homework
            contentDescription = stringResource(R.string.add_homework) // Deskripsi untuk aksesibilitas
        )
    }
}