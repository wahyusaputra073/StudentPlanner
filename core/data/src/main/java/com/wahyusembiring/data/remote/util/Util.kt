package com.wahyusembiring.data.remote.util

import com.google.firebase.firestore.DocumentSnapshot
import com.wahyusembiring.data.local.Converter
import com.wahyusembiring.data.model.ThesisWithTask
import com.wahyusembiring.data.model.entity.Exam
import com.wahyusembiring.data.model.entity.TaskThesis
import com.wahyusembiring.data.model.entity.Lecturer
import com.wahyusembiring.data.model.entity.Agenda
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.data.model.entity.Task
import com.wahyusembiring.data.model.entity.Thesis

const val USER_COLLECTION_ID = "user"  // Mendeklarasikan konstanta USER_COLLECTION_ID dengan nilai "user" sebagai identifier untuk koleksi pengguna.

fun Exam.toHashMap(converter: Converter): HashMap<String, *> {  // Menambahkan extension function toHashMap pada kelas Exam yang mengembalikan HashMap dengan tipe kunci String dan nilai yang dapat bervariasi.
    return hashMapOf(  // Membuat dan mengembalikan HashMap baru.
        "title" to title,  // Menambahkan entri dengan kunci "title" dan nilai dari properti title pada objek Exam.
        "description" to description,  // Menambahkan entri dengan kunci "description" dan nilai dari properti description.
        "due_date" to converter.dateToLong(date),  // Mengonversi properti date ke long dan menambahkannya dengan kunci "due_date".
        "reminder" to reminder?.let { converter.timeToString(it) },  // Jika reminder tidak null, konversi menjadi String dan tambahkan dengan kunci "reminder".
        "deadline" to deadline?.let { converter.timesToString(it) },  // Jika deadline tidak null, konversi menjadi String dan tambahkan dengan kunci "deadline".
        "subject_id" to subjectId,  // Menambahkan entri dengan kunci "subject_id" dan nilai dari properti subjectId.
        "attachments" to converter.listOfAttachmentToJsonString(attachments),  // Mengonversi daftar attachments ke JSON string dan menambahkannya dengan kunci "attachments".
        "score" to score,  // Menambahkan entri dengan kunci "score" dan nilai dari properti score.
        "category" to converter.examCategoryToString(category)  // Mengonversi category menjadi string dan menambahkannya dengan kunci "category".
    )
}


fun DocumentSnapshot.toExam(converter: Converter): Exam {  // Fungsi extension untuk mengonversi objek DocumentSnapshot menjadi objek Exam.
    return Exam(  // Mengembalikan objek Exam dengan properti yang diisi berdasarkan data dari DocumentSnapshot.
        id = id.toInt(),  // Mengonversi id (String) menjadi Int dan mengisinya ke properti id.
        title = get("title", String::class.java)!!,  // Mengambil nilai "title" dari snapshot dan memastikan bahwa nilainya tidak null.
        description = get("description", String::class.java) ?: "",  // Mengambil "description", jika null set ke string kosong.
        date = get("due_date", Long::class.java)  // Mengambil "due_date" sebagai Long dan mengonversinya menjadi Date menggunakan converter.
            .let { converter.longToDate(it!!) },  // Mengonversi long ke Date menggunakan fungsi longToDate milik converter.

        reminder = get("reminder", String::class.java)  // Mengambil "reminder" sebagai String dan mengonversinya menjadi Time.
            .let { converter.stringToTime(it!!) },  // Mengonversi String ke Time menggunakan fungsi stringToTime milik converter.

        deadline = get("deadline", String::class.java)  // Mengambil "deadline" sebagai String dan mengonversinya menjadi DeadlineTime.
            .let { converter.stringToTimes(it!!) },  // Mengonversi String ke DeadlineTime menggunakan fungsi stringToTimes milik converter.

        subjectId = get("subject_id", Int::class.java)!!,  // Mengambil "subject_id" sebagai Int dan mengisinya ke properti subjectId.
        attachments = get("attachments", String::class.java)  // Mengambil "attachments" sebagai String (JSON) dan mengonversinya ke list of Attachment.
            .let { converter.jsonStringToListOfAttachment(it!!) },  // Mengonversi JSON string ke list of Attachment menggunakan fungsi jsonStringToListOfAttachment milik converter.

        score = get("score", Int::class.java),  // Mengambil nilai "score" dan mengisinya ke properti score.
        category = get("grade", String::class.java)  // Mengambil "grade" sebagai String dan mengonversinya ke enum ExamCategory.
            .let { converter.stringToExamCategory(it!!) }  // Mengonversi String ke enum ExamCategory menggunakan fungsi stringToExamCategory milik converter.
    )
}

fun Task.toHashMap(converter: Converter): HashMap<String, *> {  // Fungsi extension untuk mengonversi objek Homework menjadi HashMap.
    return hashMapOf(  // Mengembalikan HashMap dengan pasangan kunci dan nilai yang sesuai.
        "title" to title,  // Menambahkan pasangan kunci "title" dan nilai properti title.
        "description" to description,  // Menambahkan pasangan kunci "description" dan nilai properti description.
        "due_date" to converter.dateToLong(dueDate),  // Mengonversi dueDate (Date) menjadi Long dan menambahkannya ke HashMap.
        "completed" to completed,  // Menambahkan pasangan kunci "completed" dan nilai properti completed.
        "reminder" to reminder?.let { converter.timeToString(it) },  // Mengonversi reminder (jika ada) ke String menggunakan converter.
        "subject_id" to subjectId,  // Menambahkan pasangan kunci "subject_id" dan nilai properti subjectId.
        "attachments" to converter.listOfAttachmentToJsonString(attachments),  // Mengonversi list attachments ke JSON string menggunakan converter.
        "score" to score  // Menambahkan pasangan kunci "score" dan nilai properti score.
    )
}


fun DocumentSnapshot.toHomework(converter: Converter): Task {  // Fungsi extension untuk mengonversi DocumentSnapshot menjadi objek Homework.
    return Task(  // Mengembalikan objek Homework yang diinisialisasi dengan data dari DocumentSnapshot.
        id = id.toInt(),  // Mengonversi id (String) menjadi Int dan mengisinya ke properti id.
        title = get("title", String::class.java)!!,  // Mengambil nilai "title" dari snapshot dan memastikan bahwa nilainya tidak null.
        description = get("description", String::class.java) ?: "",  // Mengambil "description", jika null set ke string kosong.
        dueDate = get("due_date", Long::class.java)  // Mengambil "due_date" sebagai Long dan mengonversinya menjadi Date menggunakan converter.
            .let { converter.longToDate(it!!) },  // Mengonversi long ke Date menggunakan fungsi longToDate milik converter.

        completed = get("completed", Boolean::class.java) ?: false,  // Mengambil "completed" (default false jika null) dan mengisinya ke properti completed.
        reminder = get("reminder", String::class.java)  // Mengambil "reminder" sebagai String dan mengonversinya menjadi Time.
            .let { converter.stringToTime(it!!) },  // Mengonversi String ke Time menggunakan fungsi stringToTime milik converter.

        deadline = get("deadline", String::class.java)  // Mengambil "deadline" sebagai String dan mengonversinya menjadi DeadlineTime.
            .let { converter.stringToTimes(it!!) },  // Mengonversi String ke DeadlineTime menggunakan fungsi stringToTimes milik converter.

        subjectId = get("subject_id", Int::class.java)!!,  // Mengambil "subject_id" sebagai Int dan mengisinya ke properti subjectId.
        attachments = get("attachments", String::class.java)  // Mengambil "attachments" sebagai String (JSON) dan mengonversinya ke list of Attachment.
            .let { converter.jsonStringToListOfAttachment(it!!) },  // Mengonversi JSON string ke list of Attachment menggunakan fungsi jsonStringToListOfAttachment milik converter.

        score = get("score", Int::class.java)  // Mengambil nilai "score" dan mengisinya ke properti score.
    )
}

fun Agenda.toHashMap(converter: Converter): HashMap<String, *> {  // Fungsi extension untuk mengonversi objek Reminder menjadi HashMap.
    return hashMapOf(  // Mengembalikan HashMap dengan pasangan kunci dan nilai yang sesuai.
        "title" to title,  // Menambahkan pasangan kunci "title" dan nilai properti title.
        "description" to description,  // Menambahkan pasangan kunci "description" dan nilai properti description.
        "due_date" to converter.dateToLong(date),  // Mengonversi date (Date) menjadi Long dan menambahkannya ke HashMap.
        "reminder" to converter.timeToString(time),  // Mengonversi time (Time) menjadi String dan menambahkannya ke HashMap.
        "color" to converter.colorToInt(color),  // Mengonversi color (Color) menjadi Int dan menambahkannya ke HashMap.
        "completed" to completed,  // Menambahkan pasangan kunci "completed" dan nilai properti completed.
        "attachments" to converter.listOfAttachmentToJsonString(attachments),  // Mengonversi list attachments ke JSON string.
        "duration" to converter.listOfDurationToJsonString(duration)  // Mengonversi list duration ke JSON string.
    )
}

fun DocumentSnapshot.toReminder(converter: Converter): Agenda {  // Fungsi extension untuk mengonversi DocumentSnapshot menjadi objek Reminder.
    return Agenda(  // Mengembalikan objek Reminder yang diinisialisasi dengan data dari DocumentSnapshot.
        id = id.toInt(),  // Mengonversi id (String) menjadi Int dan mengisinya ke properti id.
        title = get("title", String::class.java)!!,  // Mengambil nilai "title" dari snapshot dan memastikan bahwa nilainya tidak null.
        description = get("description", String::class.java) ?: "",  // Mengambil "description", jika null set ke string kosong.
        date = get("due_date", Long::class.java)  // Mengambil "due_date" sebagai Long dan mengonversinya menjadi Date menggunakan converter.
            .let { converter.longToDate(it!!) },  // Mengonversi long ke Date menggunakan fungsi longToDate milik converter.

        time = get("reminder", String::class.java)  // Mengambil "reminder" sebagai String dan mengonversinya menjadi Time.
            .let { converter.stringToTime(it!!) },  // Mengonversi String ke Time menggunakan fungsi stringToTime milik converter.

        duration = get("duration", String::class.java)  // Mengambil "duration" sebagai String (JSON) dan mengonversinya ke list of Duration.
            .let { converter.jsonStringToListOfDuration(it!!) },  // Mengonversi JSON string ke list of Duration menggunakan fungsi jsonStringToListOfDuration milik converter.

        color = get("color", Int::class.java)  // Mengambil "color" sebagai Int dan mengonversinya menjadi Color.
            .let { converter.intToColor(it!!) },  // Mengonversi Int ke Color menggunakan fungsi intToColor milik converter.

        completed = get("completed", Boolean::class.java) ?: false,  // Mengambil "completed" (default false jika null) dan mengisinya ke properti completed.
        attachments = get("attachments", String::class.java)  // Mengambil "attachments" sebagai String (JSON) dan mengonversinya ke list of Attachment.
            .let { converter.jsonStringToListOfAttachment(it!!) }  // Mengonversi JSON string ke list of Attachment menggunakan fungsi jsonStringToListOfAttachment milik converter.
    )
}


fun Subject.toHashMap(converter: Converter): HashMap<String, *> {  // Fungsi extension untuk mengonversi objek Subject menjadi HashMap.
    return hashMapOf(  // Mengembalikan HashMap dengan pasangan kunci dan nilai yang sesuai.
        "name" to name,  // Menambahkan pasangan kunci "name" dan nilai properti name.
        "color" to converter.colorToInt(color),  // Mengonversi color (Color) menjadi Int dan menambahkannya ke HashMap.
        "room" to room,  // Menambahkan pasangan kunci "room" dan nilai properti room.
        "lecturer_id" to lecturerId,  // Menambahkan pasangan kunci "lecturer_id" dan nilai properti lecturerId.
        "secondary_lecturer_id" to secondaryLecturerId,
        "description" to description  // Menambahkan pasangan kunci "description" dan nilai properti description.
    )
}

fun DocumentSnapshot.toSubject(converter: Converter): Subject {  // Fungsi extension untuk mengonversi DocumentSnapshot menjadi objek Subject.
    return Subject(  // Mengembalikan objek Subject yang diinisialisasi dengan data dari DocumentSnapshot.
        id = id.toInt(),  // Mengonversi id (String) menjadi Int dan mengisinya ke properti id.
        name = get("name", String::class.java)!!,  // Mengambil nilai "name" dari snapshot dan memastikan bahwa nilainya tidak null.
        color = get("color", Int::class.java)  // Mengambil "color" sebagai Int dan mengonversinya menjadi Color menggunakan converter.
            .let { converter.intToColor(it!!) },  // Mengonversi Int ke Color menggunakan fungsi intToColor milik converter.
        room = get("room", String::class.java)!!,  // Mengambil nilai "room" dari snapshot dan memastikan bahwa nilainya tidak null.
        lecturerId = get("lecturer_id", Int::class.java)!!,  // Mengambil nilai "lecturer_id" sebagai Int dari snapshot.
        secondaryLecturerId = get("secondary_lecturer_id", Int::class.java)!!,
        description = get("description", String::class.java) ?: ""  // Mengambil nilai "description", jika null set ke string kosong.
    )
}

fun Lecturer.toHashMap(converter: Converter): HashMap<String, *> {  // Fungsi extension untuk mengonversi objek Lecturer menjadi HashMap.
    return hashMapOf(  // Mengembalikan HashMap dengan pasangan kunci dan nilai yang sesuai.
        "name" to name,  // Menambahkan pasangan kunci "name" dan nilai properti name.
        "photo" to photo?.let { converter.uriToString(it) },  // Mengonversi photo (Uri) menjadi String dan menambahkannya ke HashMap (jika ada).
        "phone" to converter.listOfStringToJsonString(phone),  // Mengonversi list phone menjadi JSON string.
        "email" to converter.listOfStringToJsonString(email),  // Mengonversi list email menjadi JSON string.
        "address" to converter.listOfStringToJsonString(address),  // Mengonversi list address menjadi JSON string.
        "office_hour" to converter.listOfOfficeHourToJsonString(officeHour),  // Mengonversi list office_hour menjadi JSON string.
        "website" to converter.listOfStringToJsonString(website)  // Mengonversi list website menjadi JSON string.
    )
}


fun DocumentSnapshot.toLecturer(converter: Converter): Lecturer {  // Fungsi untuk mengonversi DocumentSnapshot ke objek Lecturer
    return Lecturer(  // Mengembalikan objek Lecturer
        id = id.toInt(),  // Mengonversi id ke tipe Int
        name = get("name", String::class.java)!!,  // Mengambil nama sebagai String
        photo = get("photo", String::class.java)  // Mengambil foto sebagai String, null jika tidak ada
            ?.let { converter.stringToUri(it) },  // Mengonversi foto string ke URI
        phone = get("phone", String::class.java)  // Mengambil nomor telepon sebagai String
            .let { converter.jsonStringToListOfString(it!!) },  // Mengonversi string JSON ke List<String>
        email = get("email", String::class.java)  // Mengambil email sebagai String
            .let { converter.jsonStringToListOfString(it!!) },  // Mengonversi string JSON ke List<String>
        address = get("address", String::class.java)  // Mengambil alamat sebagai String
            .let { converter.jsonStringToListOfString(it!!) },  // Mengonversi string JSON ke List<String>
        officeHour = get("office_hour", String::class.java)  // Mengambil jam kerja sebagai String
            .let { converter.jsonStringToListOfOfficeHour(it!!) },  // Mengonversi string JSON ke List<OfficeHour>
        website = get("website", String::class.java)  // Mengambil website sebagai String
            .let { converter.jsonStringToListOfString(it!!) },  // Mengonversi string JSON ke List<String>
    )
}

fun ThesisWithTask.toHashMap(converter: Converter): HashMap<String, *> {  // Fungsi untuk mengonversi ThesisWithTask ke HashMap
    return hashMapOf(  // Mengembalikan HashMap
        "title" to thesis.title,  // Menyimpan judul tesis
        "articles" to converter.listOfFileToJsonString(thesis.articles),  // Mengonversi daftar artikel ke string JSON
        "tasks" to taskTheses.map {  // Mengonversi daftar tugas ke format map
            mapOf(  // Setiap tugas diubah menjadi map
                "task_id" to it.id,  // Menyimpan id tugas
                "thesis_id" to it.thesisId,  // Menyimpan id tesis
                "name" to it.name,  // Menyimpan nama tugas
                "is_completed" to it.isCompleted,  // Menyimpan status penyelesaian tugas
                "due_date" to converter.dateToLong(it.dueDate)  // Mengonversi tanggal jatuh tempo ke format long
            )
        }
    )
}


@Suppress("UNCHECKED_CAST")  // Menonaktifkan peringatan casting yang tidak aman
fun DocumentSnapshot.toThesisWithTask(converter: Converter): ThesisWithTask {  // Fungsi untuk mengonversi DocumentSnapshot ke ThesisWithTask
    val thesis = Thesis(  // Membuat objek Thesis
        id = id.toInt(),  // Mengonversi id ke tipe Int
        title = get("title", String::class.java)!!,  // Mengambil judul tesis sebagai String
        articles = get("articles", String::class.java)  // Mengambil artikel sebagai String
            .let { converter.jsonStringToListOfFile(it!!) }  // Mengonversi string JSON ke List<File>
    )
    val taskTheses = (get("tasks") as List<Map<String, Any>>).map { taskDto ->  // Mengambil daftar tugas dan mengonversinya
        TaskThesis(  // Membuat objek Task untuk setiap tugas
            id = (taskDto["task_id"] as Long).toInt(),  // Mengonversi task_id ke Int
            thesisId = (taskDto["thesis_id"] as Long).toInt(),  // Mengonversi thesis_id ke Int
            name = taskDto["name"] as String,  // Mengambil nama tugas sebagai String
            isCompleted = taskDto["is_completed"] as Boolean,  // Mengambil status penyelesaian tugas
            dueDate = converter.longToDate(taskDto["due_date"] as Long)  // Mengonversi due_date ke Date
        )
    }
    return ThesisWithTask(thesis, taskTheses)  // Mengembalikan objek ThesisWithTask dengan tesis dan daftar tugas
}
