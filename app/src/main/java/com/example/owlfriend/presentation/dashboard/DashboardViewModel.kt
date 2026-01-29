package com.example.owlfriend.presentation.dashboard

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owlfriend.MainActivity
import com.example.owlfriend.domain.Session
import com.example.owlfriend.domain.Subject
import com.example.owlfriend.domain.Task
import com.example.owlfriend.domain.repository.SessionRepository
import com.example.owlfriend.domain.repository.SubjectRepository
import com.example.owlfriend.domain.repository.TaskRepository
import com.example.owlfriend.util.SnackbarEvent
import com.example.owlfriend.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
) : ViewModel(){

    private val _state = MutableStateFlow(DashboardState())
    val state = stateFlow()

    private fun stateFlow() = combine(
            _state,
            subjectRepository.getTotalSubjectCount(),
            subjectRepository.getTotalGoalHours(),
            subjectRepository.getAllSubjects(),
            sessionRepository.getTotalSessionsDuration()
        ) { state, subjectCount, goalHours, subjects, totalSessionDuration ->
            state.copy(
                totalSubjectCount = subjectCount,
                totalGoalStudyHours = goalHours,
                subjects = subjects,
                totalStudiedHours = totalSessionDuration.toHours(),
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardState()
        )

    val tasks: StateFlow<List<Task>> = taskRepository.getAllUpcomingTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    val recentSessions: StateFlow<List<Session>> = sessionRepository.getRecentFiveSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    private val _snackbarEventFlow = MutableSharedFlow<SnackbarEvent>()
    val snackbarEventFlow = _snackbarEventFlow.asSharedFlow()

    fun onEvent(event: DashboardEvent) {
        when(event)  {
            is DashboardEvent.OnSubjectNameChange -> {
                _state.update { it.copy(subjectName = event.name) }
            }

            is DashboardEvent.OnGoalStudyHoursChange -> {
                _state.update { it.copy(goalStudyHours = event.hours) }
            }

            is DashboardEvent.OnSubjectCardColorChange -> {
                _state.update { it.copy(subjectCardColors = event.colors) }
            }

            DashboardEvent.DeleteSubject -> {

            }
            is DashboardEvent.OnDeleteSessionButtonClick -> TODO()



            is DashboardEvent.OnTaskIsCompleteChange -> TODO()
            DashboardEvent.SaveSubject -> saveSubject()
        }
    }

    private fun saveSubject() {
        viewModelScope.launch {
           try {
               subjectRepository.upsertSubject(
                   subject = Subject(
                       name = state.value.subjectName,
                       goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                       colors = state.value.subjectCardColors.map { it.toArgb() },
                   )
               )

               _state.update {
                   it.copy(
                       subjectName = "",
                       goalStudyHours = "",
                       subjectCardColors = MainActivity.subjectCardColors.random()
                   )
               }
               _snackbarEventFlow.emit(
                   SnackbarEvent.ShowSnackbar(
                       message = "Subject is saved successfully",
                       duration = SnackbarDuration.Long
                   )
               )

           } catch (e:Exception) {
               _snackbarEventFlow.emit(
                   SnackbarEvent.ShowSnackbar(
                       message = "Couldn't save subject. ${e.message}",
                       duration = SnackbarDuration.Long
                   )
               )
           }
        }
    }

}