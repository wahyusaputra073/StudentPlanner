package com.wahyusembiring.thesisplanner.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import com.wahyusembiring.data.model.entity.Task
import com.wahyusembiring.datetime.Moment
import com.wahyusembiring.datetime.formatter.FormattingStyle
import com.wahyusembiring.thesisplanner.R


@Composable
internal fun TaskList(
    tasks: List<Task>, // Daftar task yang akan ditampilkan
    onCompletedStatusChange: (Task, Boolean) -> Unit, // Fungsi callback untuk mengubah status tugas (selesai/tidak)
    onDeleteTaskClick: (Task) -> Unit // Fungsi callback untuk menghapus task
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(), // Menyusun daftar task secara vertikal dengan lebar penuh
        horizontalAlignment = Alignment.CenterHorizontally // Menyelaraskan item secara horizontal di tengah
    ) {
        items(
            items = tasks, // Item yang akan ditampilkan adalah daftar task
            key = { it.id } // Setiap item memiliki key unik berdasarkan ID
        ) { task -> // Setiap task akan dirender di dalam item list
            var moreOptionExpanded by remember { mutableStateOf(false) } // State untuk mengatur dropdown menu

            ListItem(
                overlineContent = { // Tanggal jatuh tempo task
                    Text(
                        text = Moment
                            .fromEpochMilliseconds(task.dueDate.time) // Mengonversi timestamp ke tanggal
                            .toString(FormattingStyle.INDO_SHORT), // Format tanggal dalam gaya Indonesia
                    )
                },
                headlineContent = { // Nama task dengan gaya teks yang berbeda jika sudah selesai
                    Text(
                        text = task.name,
                        style = MaterialTheme.typography.bodyMedium
                            .copy(
                                textDecoration = if (task.isCompleted) { // Menambahkan garis melalui jika selesai
                                    TextDecoration.LineThrough
                                } else {
                                    TextDecoration.None
                                }
                            )
                    )
                },
                leadingContent = { // Checkbox untuk menandai task selesai atau belum
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = {
                            onCompletedStatusChange(task, it) // Menangani perubahan status selesai
                        }
                    )
                },
                trailingContent = { // Tombol opsi tambahan (tiga titik)
                    Column {
                        IconButton(
                            onClick = {
                                moreOptionExpanded = true // Menampilkan dropdown menu
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_more), // Ikon lebih
                                contentDescription = stringResource(R.string.more_option) // Deskripsi untuk aksesibilitas
                            )
                        }
                        DropdownMenu(
                            expanded = moreOptionExpanded, // Status dropdown menu
                            onDismissRequest = { moreOptionExpanded = false } // Menutup menu jika area luar diklik
                        ) {
                            DropdownMenuItem(
                                leadingIcon = { // Ikon hapus task
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_delete),
                                        contentDescription = stringResource(R.string.delete_task) // Deskripsi hapus task
                                    )
                                },
                                text = { Text(text = stringResource(R.string.delete)) }, // Teks menu "Hapus"
                                onClick = {
                                    onDeleteTaskClick(task) // Menangani penghapusan task
                                }
                            )
                        }
                    }
                }
            )
        }
    }
}