package com.wahyusembiring.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Date

@Entity(
    tableName = "task",  // Menandakan bahwa kelas Task akan dipetakan ke tabel "task" dalam database Room.
    foreignKeys = [
        ForeignKey(  // Menentukan relasi antara tabel task dan thesis melalui foreign key.
            entity = Thesis::class,  // Entitas Thesis yang menjadi referensi.
            parentColumns = ["id"],  // Kolom "id" pada tabel Thesis menjadi kolom induk (parent).
            childColumns = ["thesis_id"],  // Kolom "thesis_id" pada tabel Task menjadi kolom anak (child).
            onDelete = ForeignKey.CASCADE,  // Jika entitas Thesis dihapus, maka entitas Task yang terkait juga akan dihapus.
            onUpdate = ForeignKey.CASCADE  // Jika entitas Thesis diperbarui, maka entitas Task yang terkait juga akan diperbarui.
        )
    ]
)

@Serializable  // Menandakan bahwa kelas Task bisa diserialisasi menggunakan Kotlin Serialization.
data class Task(
    @PrimaryKey(autoGenerate = true)  // Menandakan bahwa "id" adalah primary key dan nilainya akan otomatis di-generate oleh Room.
    val id: Int = 0,  // ID unik untuk setiap task, defaultnya adalah 0.

    @SerialName("thesis_id")  // Nama properti pada file JSON atau database.
    @ColumnInfo(name = "thesis_id")  // Nama kolom pada database yang berisi foreign key dari tabel Thesis.
    val thesisId: Int,  // ID dari thesis yang terkait dengan task ini.

    val name: String,  // Nama dari task yang diberikan.

    @SerialName("is_completed")  // Nama properti dalam JSON atau database yang menyatakan status selesai atau belum.
    @ColumnInfo(name = "is_completed")  // Nama kolom pada database untuk status penyelesaian task.
    val isCompleted: Boolean = false,  // Status apakah task sudah selesai atau belum, defaultnya false.

    @SerialName("due_date")  // Nama properti dalam JSON atau database yang menunjukkan tanggal jatuh tempo.
    @Serializable(with = DateSerializer::class)  // Menyatakan bahwa properti dueDate akan diserialisasi/dideserialisasi menggunakan `DateSerializer`.
    @ColumnInfo(name = "due_date")  // Nama kolom pada database yang menyimpan tanggal jatuh tempo.
    val dueDate: Date  // Tanggal jatuh tempo task.
)


object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG)  // Deskripsi tipe data, menyatakan bahwa tipe data yang diserialisasi adalah tipe primitif LONG (milliseconds).

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeLong(value.time)  // Mengonversi objek Date menjadi long (waktu dalam milidetik sejak epoch).
    }

    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeLong())  // Mengonversi kembali nilai long menjadi objek Date.
    }
}
