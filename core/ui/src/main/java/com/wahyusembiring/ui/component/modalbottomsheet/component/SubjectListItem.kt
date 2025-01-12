package com.wahyusembiring.ui.component.modalbottomsheet.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.ui.R

@Composable
fun SubjectListItem(
    modifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(), // Menentukan warna default untuk item list
    subject: Subject, // Data subject yang akan ditampilkan
    onClicked: ((subject: Subject) -> Unit)? = null, // Opsional: aksi saat item diklik
    onDeleteSubClick: () -> Unit, // Aksi untuk menghapus subject
) {
    var expanded by remember { mutableStateOf(false) } // Mengatur state apakah menu dropdown ditampilkan

    ListItem(
        colors = colors, // Menggunakan warna yang ditentukan
        modifier = modifier
            .then(
                if (onClicked != null) { // Jika onClicked tidak null, item menjadi clickable
                    Modifier.clickable { onClicked(subject) }
                } else {
                    Modifier
                }
            ),
        leadingContent = { // Konten yang ditampilkan di sebelah kiri item
            Icon(
                painter = painterResource(id = R.drawable.ic_subjects), // Ikon subjek
                contentDescription = subject.name, // Deskripsi konten untuk aksesibilitas
                tint = subject.color // Warna ikon berdasarkan subjek
            )
        },
        headlineContent = { // Konten utama yang ditampilkan, yaitu nama subjek
            Text(text = subject.name)
        },
        trailingContent = { // Konten di sebelah kanan, ikon untuk menampilkan menu
            IconButton(
                onClick = { expanded = true } // Menampilkan dropdown menu saat diklik
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vertical), // Ikon menu
                    contentDescription = null
                )
            }

            // Dropdown menu yang muncul setelah ikon diklik
            DropdownMenu(
                expanded = expanded, // Menyimpan status apakah menu terbuka
                onDismissRequest = { expanded = false } // Menutup menu jika diluar area
            ) {
                // Menu item untuk menghapus subject
                DropdownMenuItem(
                    onClick = {
                        expanded = false // Menutup menu setelah aksi dipilih
                        onDeleteSubClick() // Memanggil aksi penghapusan subjek
                    },
                    text = {
                        Text(text = stringResource(R.string.delete)) // Text untuk opsi delete
                    }
                )
            }
        }
    )
}


@Composable
fun AddNewSubject(
    modifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(), // Color customization for the list item
    onClicked: (() -> Unit)? = null, // Action when the list item is clicked
) {
    ListItem(
        colors = colors, // Applying colors to the list item
        modifier = modifier
            .then(
                if (onClicked != null) { // Make the item clickable if onClicked is provided
                    Modifier.clickable { onClicked() }
                } else {
                    Modifier
                }
            ),
        leadingContent = { // The leading content of the list item, typically an icon
            Icon(
                painter = painterResource(id = R.drawable.ic_add), // Icon to represent adding
                contentDescription = stringResource(R.string.add_new_subject), // Accessibility description
                tint = MaterialTheme.colorScheme.primary // Primary color for the icon
            )
        },
        headlineContent = { // The main text content of the list item
            Text(text = stringResource(R.string.add_new_subject)) // Display text for the action
        }
    )
}