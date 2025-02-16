package com.wahyusembiring.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.wahyusembiring.data.model.entity.Lecturer
import com.wahyusembiring.data.model.entity.Subject

data class LecturerWithSubject(
    @Embedded val lecturer: Lecturer,

    @Relation(
        parentColumn = "id",
        entityColumn = "lecturer_id"
    )
    val primarySubjects: List<Subject>,

    @Relation(
        parentColumn = "id",
        entityColumn = "secondary_lecturer_id"
    )
    val secondarySubjects: List<Subject>
)