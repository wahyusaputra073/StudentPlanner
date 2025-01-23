package com.wahyusembiring.ui.component.profilepicturepicker

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.component.popup.alertdialog.information.InformationAlertDialog
import com.wahyusembiring.ui.theme.spacing
import com.wahyusembiring.ui.util.getActivity
import com.wahyusembiring.ui.util.getPhotoAccessPermissionRequest

@Composable
fun ProfilePicturePicker(
    modifier: Modifier = Modifier, // Modifier untuk dekorasi composable
    imageUri: Uri?, // URI gambar yang sedang dipilih
    onImageSelected: (uri: Uri?) -> Unit // Callback untuk mengirim URI gambar yang dipilih
) {
    val context = LocalContext.current // Konteks aplikasi saat ini
    var permissionRationale: List<String>? by remember {
        mutableStateOf(null) // State untuk alasan permintaan izin
    }

    val photoPickerLauncher = // Peluncur aktivitas untuk memilih gambar
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(), // Kontrak memilih media visual
            onResult = {
                context.contentResolver.takePersistableUriPermission( // Memberikan izin baca pada URI
                    it ?: return@rememberLauncherForActivityResult,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                onImageSelected(it) // Mengirim URI gambar yang dipilih
            }
        )

    val imagePermissionRequestLauncher = // Peluncur untuk permintaan beberapa izin
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(), // Kontrak untuk permintaan izin
            onResult = {
                onImagePermissionRequestLauncherResult(
                    context = context,
                    permissionsResult = it, // Hasil permintaan izin
                    onAllPermissionsGranted = {
                        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) // Meluncurkan pemilih gambar
                    },
                    onShouldShowPermissionRationale = { permissions -> // Alasan izin harus ditampilkan
                        permissionRationale = permissions
                    },
                    onUserPermanentlyDeniedPermissions = { } // Tindakan jika pengguna menolak izin secara permanen
                )
            }
        )

    Box(
        modifier = modifier
            .clip(CircleShape) // Memotong kotak ke bentuk lingkaran
            .clickable { // Menangani klik pada kotak
                onPhotoClick(
                    context = context,
                    permissionsLauncher = imagePermissionRequestLauncher,
                    photoPickerLauncher = photoPickerLauncher,
                )
            },
        contentAlignment = Alignment.Center // Menyusun konten di tengah
    ) {
        val scrimColor = MaterialTheme.colorScheme.scrim // Warna scrim
        val cameraImageVector: ImageVector = ImageVector.vectorResource(id = R.drawable.ic_photo) // Ikon kamera
        val cameraImagePainter = rememberVectorPainter(image = cameraImageVector) // Painter untuk ikon kamera

        Box(
            modifier = Modifier
                .fillMaxSize() // Mengisi seluruh ukuran komponen
                .drawWithContent {
                    drawContent() // Menggambar konten utama
                    drawArc( // Menggambar area setengah lingkaran di bagian bawah
                        color = scrimColor.copy(alpha = 0.2f),
                        useCenter = true,
                        startAngle = 0f,
                        sweepAngle = 180f,
                        topLeft = Offset(0f, size.height / 2)
                    )
                    val iconWidth = size.height / 4 * 0.7f // Lebar ikon kamera

                    translate( // Memindahkan posisi ikon kamera
                        left = (size.width / 2) - (iconWidth / 2),
                        top = (size.height / 4 * 3) + ((size.height - (size.height / 4 * 3)) / 2) - ((iconWidth / 2) + (iconWidth / 2 * 0.15f)),
                    ) {
                        with(cameraImagePainter) { // Menggambar ikon kamera
                            draw(
                                size = Size(iconWidth, iconWidth),
                                alpha = 0.5f,
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                    }
                },
            contentAlignment = Alignment.Center // Konten di tengah
        ) {
            if (imageUri == null) { // Jika gambar belum dipilih
                Box(
                    modifier = Modifier
                        .fillMaxSize() // Mengisi ukuran penuh
                        .background(MaterialTheme.colorScheme.secondary), // Warna latar belakang
                    contentAlignment = Alignment.Center // Konten di tengah
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.spacing.Medium), // Padding ikon
                        painter = painterResource(id = R.drawable.ic_person), // Ikon pengguna default
                        tint = MaterialTheme.colorScheme.onPrimary, // Warna ikon
                        contentDescription = null
                    )
                }
            } else { // Jika gambar sudah dipilih
                AsyncImage(
                    model = imageUri, // URI gambar
                    contentDescription = null,
                    contentScale = ContentScale.Crop // Gambar dipotong agar sesuai
                )
            }
        }
    }
    if (permissionRationale != null) { // Menampilkan dialog alasan izin jika diperlukan
        InformationAlertDialog(
            onButtonClicked = {
                onPhotoClick(
                    context = context,
                    permissionToRequest = permissionRationale ?: return@InformationAlertDialog,
                    permissionsLauncher = imagePermissionRequestLauncher,
                    photoPickerLauncher = photoPickerLauncher,
                )
            },
            buttonText = "Ok", // Teks tombol dalam dialog
            title = "Permission Required", // Judul dialog
            message = "Our app need permission to access your photo. Please grant the permission.", // Pesan dialog
            onDismissRequest = { permissionRationale = null } // Menutup dialog
        )
    }
}


private fun onPhotoClick(
    context: Context, // Konteks aplikasi saat ini
    permissionToRequest: List<String> = getPhotoAccessPermissionRequest(), // Daftar izin yang diperlukan
    permissionsLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>, // Peluncur untuk meminta izin
    photoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> // Peluncur untuk memilih media
) {
    if (permissionToRequest.all { isPermissionGranted(context, it) }) { // Jika semua izin sudah diberikan
        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) // Meluncurkan pemilih media untuk gambar saja
    } else {
        val deniedPermissions = permissionToRequest.filter { !isPermissionGranted(context, it) } // Mendapatkan izin yang ditolak
        permissionsLauncher.launch(deniedPermissions.toTypedArray()) // Meminta izin yang belum diberikan
    }
}


private fun isPermissionGranted(
    context: Context, // Konteks aplikasi saat ini
    permission: String // Nama izin yang ingin diperiksa
): Boolean {
    return ContextCompat.checkSelfPermission( // Mengecek status izin
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED // Mengembalikan true jika izin diberikan
}

private fun onImagePermissionRequestLauncherResult(
    context: Context, // Konteks aplikasi saat ini
    permissionsResult: Map<String, Boolean>, // Hasil dari permintaan izin
    onAllPermissionsGranted: () -> Unit, // Callback jika semua izin diberikan
    onShouldShowPermissionRationale: (permissions: List<String>) -> Unit, // Callback untuk izin yang memerlukan penjelasan tambahan
    onUserPermanentlyDeniedPermissions: (permissions: List<String>) -> Unit, // Callback untuk izin yang ditolak secara permanen
) {
    if (permissionsResult.all { it.value }) { // Jika semua izin diberikan
        onAllPermissionsGranted() // Menjalankan callback untuk izin yang diberikan
    } else {
        val deniedPermissions = permissionsResult.filter { !it.value }.keys // Mendapatkan daftar izin yang ditolak
        val (permissionToShowRationale, permanentlyDeniedPermissions) = // Memisahkan izin yang memerlukan penjelasan dari yang ditolak permanen
            deniedPermissions.partition {
                ActivityCompat.shouldShowRequestPermissionRationale(context.getActivity()!!, it) // Mengecek apakah penjelasan izin diperlukan
            }
        onShouldShowPermissionRationale(permissionToShowRationale) // Callback untuk izin dengan penjelasan tambahan
        onUserPermanentlyDeniedPermissions(permanentlyDeniedPermissions) // Callback untuk izin yang ditolak permanen
    }
}