package com.example.owlfriend.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.example.owlfriend.domain.Session
import com.example.owlfriend.domain.Task

sealed class DashboardEvent {
    data object SaveSubject : DashboardEvent()
    data object DeleteSubject : DashboardEvent()
    data class OnDeleteSessionButtonClick(val session: Session): DashboardEvent()
    data class OnTaskIsCompleteChange(val task: Task): DashboardEvent()
    data class OnSubjectCardColorChange(val colors: List<Color>): DashboardEvent()
    data class OnSubjectNameChange(val name: String): DashboardEvent()
    data class OnGoalStudyHoursChange(val hours: String): DashboardEvent()
}