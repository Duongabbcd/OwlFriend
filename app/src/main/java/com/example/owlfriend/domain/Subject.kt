package com.example.owlfriend.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Subject(
    val name: String,
    val goalHours: Float,
    val colors: List<Int>,
    @PrimaryKey(autoGenerate = true)
    val subjectId: Int? = null
)