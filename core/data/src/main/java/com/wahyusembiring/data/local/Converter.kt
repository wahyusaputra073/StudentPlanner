package com.wahyusembiring.data.local

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.File
import com.wahyusembiring.data.model.entity.ExamCategory
import com.wahyusembiring.data.model.OfficeHour
import com.wahyusembiring.data.model.SpanTime
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.data.util.toAttachment
import com.wahyusembiring.data.util.toFile
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date

@ProvidedTypeConverter
class Converter(
    private val appContext: Context
) {

    @TypeConverter
    fun uriToString(uri: Uri): String = uri.toString()  // Mengonversi objek Uri menjadi String.

    @TypeConverter
    fun stringToUri(string: String): Uri = Uri.parse(string)  // Mengonversi String menjadi objek Uri.

    @TypeConverter
    fun dateToLong(date: Date): Long = date.time  // Mengonversi objek Date menjadi milidetik (Long).

    @TypeConverter
    fun longToDate(long: Long): Date = Date(long)  // Mengonversi Long (milidetik) menjadi objek Date.

    @TypeConverter
    fun timeToString(time: Time): String = "${time.hour}:${time.minute}"  // Mengonversi objek Time menjadi String "jam:menit".

    @TypeConverter
    fun stringToTime(string: String): Time {
        val (hour, minute) = string.split(":")
        return Time(hour.toInt(), minute.toInt())  // Mengonversi String "jam:menit" menjadi objek Time.
    }

    @TypeConverter
    fun timesToString(times: DeadlineTime): String = "${times.hour}:${times.minute}"  // Mengonversi objek DeadlineTime menjadi String.

    @TypeConverter
    fun stringToTimes(string: String): DeadlineTime {
        val (hour, minute) = string.split(":")
        return DeadlineTime(hour.toInt(), minute.toInt())  // Mengonversi String "jam:menit" menjadi objek DeadlineTime.
    }

    @TypeConverter
    fun colorToInt(color: Color): Int = color.toArgb()  // Mengonversi objek Color menjadi Integer dalam format ARGB.

    @TypeConverter
    fun intToColor(int: Int): Color = Color(int)  // Mengonversi Integer (ARGB) menjadi objek Color.

    @TypeConverter
    fun listOfStringToJsonString(listOfString: List<String>): String = Json.encodeToString(listOfString)  // Mengonversi List<String> menjadi String JSON.

    @TypeConverter
    fun jsonStringToListOfString(jsonString: String): List<String> = Json.decodeFromString(jsonString)  // Mengonversi String JSON menjadi List<String>.

    @TypeConverter
    fun listOfUriToJsonString(listOfUri: List<Uri>): String {
        val uris = listOfUri.map { it.toString() }
        return Json.encodeToString(uris)  // Mengonversi List<Uri> menjadi String JSON.
    }

    @TypeConverter
    fun jsonStringToListOfUri(jsonString: String): List<Uri> {
        val uris = Json.decodeFromString<List<String>>(jsonString)
        return uris.map { Uri.parse(it) }  // Mengonversi String JSON menjadi List<Uri>.
    }

    @TypeConverter
    fun listOfAttachmentToJsonString(listOfAttachment: List<Attachment>): String {
        val listOfUri = listOfAttachment.map { it.uri }
        return listOfUriToJsonString(listOfUri)  // Mengonversi List<Attachment> menjadi String JSON.
    }

    @TypeConverter
    fun jsonStringToListOfAttachment(jsonString: String): List<Attachment> {
        val uris = jsonStringToListOfUri(jsonString)
        return uris.map { it.toAttachment(appContext) }  // Mengonversi List<Uri> menjadi List<Attachment>.
    }

    @TypeConverter
    fun listOfFileToJsonString(listOfFile: List<File>): String {
        val listOfUri = listOfFile.map { it.uri }
        return listOfUriToJsonString(listOfUri)  // Mengonversi List<File> menjadi String JSON.
    }

    @TypeConverter
    fun jsonStringToListOfFile(jsonString: String): List<File> {
        val uris = jsonStringToListOfUri(jsonString)
        return uris.map { it.toFile(appContext) }  // Mengonversi List<Uri> menjadi List<File>.
    }

    @TypeConverter
    fun examCategoryToString(category: ExamCategory): String = category.name  // Mengonversi objek ExamCategory menjadi String.

    @TypeConverter
    fun stringToExamCategory(string: String): ExamCategory = ExamCategory.valueOf(string)  // Mengonversi String menjadi objek ExamCategory.

    @TypeConverter
    fun listOfOfficeHourToJsonString(listOfOfficeHour: List<OfficeHour>): String = Json.encodeToString(listOfOfficeHour)  // Mengonversi List<OfficeHour> menjadi String JSON.

    @TypeConverter
    fun jsonStringToListOfOfficeHour(jsonString: String): List<OfficeHour> = Json.decodeFromString(jsonString)  // Mengonversi String JSON menjadi List<OfficeHour>.

    @TypeConverter
    fun listOfDurationToJsonString(listOfDuration: SpanTime): String = Json.encodeToString(listOfDuration)  // Mengonversi SpanTime menjadi String JSON.

    @TypeConverter
    fun jsonStringToListOfDuration(jsonString: String): SpanTime = Json.decodeFromString(jsonString)  // Mengonversi String JSON menjadi SpanTime.
}
