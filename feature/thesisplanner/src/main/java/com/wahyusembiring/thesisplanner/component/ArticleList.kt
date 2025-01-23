package com.wahyusembiring.thesisplanner.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.wahyusembiring.data.model.File
import com.wahyusembiring.thesisplanner.R

@Composable
internal fun ArticleList(
    articles: List<File>,  // Daftar artikel dalam bentuk File
    onArticleClick: (File) -> Unit,  // Fungsi untuk menangani klik pada artikel
    onDeleteArticleClick: (File) -> Unit  // Fungsi untuk menangani klik hapus artikel
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),  // Mengisi lebar penuh
        horizontalAlignment = Alignment.CenterHorizontally  // Mengatur konten secara horizontal di tengah
    ) {
        items(
            items = articles,  // Menggunakan list artikel
            key = { it.uri }  // Menggunakan URI sebagai key untuk setiap item
        ) { article ->
            var moreOptionExpanded by remember { mutableStateOf(false) }  // State untuk mengelola menu opsi tambahan

            ListItem(
                modifier = Modifier
                    .clickable { onArticleClick(article) },  // Menangani klik artikel
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pdf),  // Ikon PDF untuk artikel
                        contentDescription = stringResource(R.string.pdf_icon)  // Deskripsi ikon
                    )
                },
                headlineContent = {
                    Text(text = article.fileName)  // Menampilkan nama artikel (fileName)
                },
                trailingContent = {
                    Column {
                        IconButton(
                            onClick = { moreOptionExpanded = true }  // Mengubah state untuk menampilkan menu opsi tambahan
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_more),  // Ikon untuk opsi lebih
                                contentDescription = stringResource(R.string.more_option)  // Deskripsi ikon
                            )
                        }
                        DropdownMenu(
                            expanded = moreOptionExpanded,  // Menampilkan menu berdasarkan state
                            onDismissRequest = { moreOptionExpanded = false }  // Menyembunyikan menu jika dismissed
                        ) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_delete),  // Ikon hapus
                                        contentDescription = stringResource(R.string.delete_article)  // Deskripsi ikon hapus
                                    )
                                },
                                text = {
                                    Text(text = stringResource(R.string.delete))  // Teks untuk opsi hapus
                                },
                                onClick = {
                                    onDeleteArticleClick(article)  // Menangani klik hapus artikel
                                }
                            )
                        }
                    }
                }
            )
        }
    }
}