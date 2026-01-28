package com.example.owlfriend.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.example.owlfriend.MainActivity
import com.example.owlfriend.domain.Session
import com.example.owlfriend.domain.Subject

data class DashboardState(
    val totalSubjectCount: Int = 0,
    val totalStudiedHours: Float = 0f,
    val totalGoalStudyHours: Float = 0f,
    val subjects: List<Subject> = emptyList(),
    val subjectName: String = "",
    val goalStudyHours: String = "",
    val subjectCardColors: List<Color> = MainActivity.subjectCardColors.random(),
    val session: Session? = null
)