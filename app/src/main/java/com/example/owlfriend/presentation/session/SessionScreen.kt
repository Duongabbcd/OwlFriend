package com.example.owlfriend.presentation.session

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.owlfriend.MainActivity
import com.example.owlfriend.presentation.components.DeleteDialog
import com.example.owlfriend.presentation.components.SubjectListBottomSheet
import com.example.owlfriend.presentation.components.studySessionsList
import com.example.owlfriend.presentation.session.service.ServiceHelper
import com.example.owlfriend.presentation.session.service.StudySessionTimerService
import com.example.owlfriend.presentation.session.service.TimerState
import com.example.owlfriend.presentation.theme.Red
import com.example.owlfriend.util.Constants
import com.example.owlfriend.util.SnackbarEvent
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@Destination(
    deepLinks = [
        DeepLink(
            action = Intent.ACTION_VIEW,
            uriPattern = "owl_friend://dashboard/session"
        )
    ]
)

@Composable
fun SessionScreenRoute(
    navigator: DestinationsNavigator, timerService: StudySessionTimerService
) {
    val viewModel: SessionViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()


    SessionScreen(
        state = state,
        snackBarEvent = viewModel.snackBarEventFlow,
        onEvent = viewModel::onEvent,
        onBackButtonClick = { navigator.navigateUp() },
        timerService = timerService
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreen(
    state: SessionState,
    snackBarEvent: SharedFlow<SnackbarEvent>,
    onEvent: (SessionEvent) -> Unit,
    onBackButtonClick: () -> Unit,
    timerService: StudySessionTimerService
) {
    val context = LocalContext.current

    val hours by timerService.hours
    val minutes by timerService.minutes
    val seconds by timerService.seconds

    val currentTimerState by timerService.currentTimerState

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }
    var isDeleteDialogOpen by remember { mutableStateOf(false) }


    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        subjects = MainActivity.subjects,
        onDismissRequest = {
            isBottomSheetOpen = false
        },
        onSubjectClicked = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isBottomSheetOpen = false
            }
        }
    )

    DeleteDialog(
        isOpen = isDeleteDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure you want to delete this session? " +
                "This action can not be undone.",
        onDismissRequest = { isDeleteDialogOpen = false },
        onConfirmButtonClick = {
            isDeleteDialogOpen = false
        }
    )

    Scaffold(topBar = {
        SessionScreenTopBar(onBackButtonClick = onBackButtonClick)
    }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                TimerSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f), hours, minutes,seconds
                )
            }
            item {
                RelatedToSubjectSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    relatedToSubject = "English", {
                        isBottomSheetOpen = true
                    })
            }
            item {
                ButtonSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    startButtonClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = Constants.ACTION_SERVICE_START
                        )
                    },
                    finishButtonClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = Constants.ACTION_SERVICE_STOP
                        )
                    },
                    cancelButtonClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = Constants.ACTION_SERVICE_CANCEL
                        )
                    },
                    timerState = currentTimerState,
                    seconds = seconds
                )
            }

            studySessionsList(
                "STUDY SESSIONS HISTORY",
                emptyListText = "You don't have any recent study sessions\n " +
                        "Start a study session to begin recording your progress",
                sessions = MainActivity.sessions,
                onDeleteIconClick = { isDeleteDialogOpen = true }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreenTopBar(
    onBackButtonClick: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(imageVector = Icons.Default.ArrowBack, "Navigate to Back Screen")
            }
        },
        title = {
            Text(text = "Study Sessions", style = MaterialTheme.typography.headlineSmall)
        }
    )
}


@Composable
private fun TimerSection(
    modifier: Modifier,
    hours: String,
    minutes: String,
    seconds: String,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .border(
                    5.dp, MaterialTheme.colorScheme.surfaceVariant,
                    CircleShape
                )
        )
        Row {
            AnimatedContent(targetState =hours, label = hours ) { hour ->
                Text(text = hour, style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp))
            }

            AnimatedContent(targetState =minutes, label = minutes ) { minute ->
                Text(text = minute, style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp))
            }

            AnimatedContent(targetState =seconds, label = seconds ) { second ->
                Text(text = second, style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp))
            }
        }

    }
}

@Composable
private fun RelatedToSubjectSection(
    modifier: Modifier,
    relatedToSubject: String,
    selectSubjectButtonClick: () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = "Related to subject",
            style = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(relatedToSubject, style = MaterialTheme.typography.bodyLarge)
            IconButton(selectSubjectButtonClick) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Subject"
                )
            }
        }
    }

}

@Composable
private fun ButtonSection(
    modifier: Modifier = Modifier,
    startButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit,
    finishButtonClick: () -> Unit,
    timerState: TimerState,
    seconds: String
) {
    Row(modifier = modifier, Arrangement.SpaceBetween) {
        Button(cancelButtonClick, enabled = seconds != "00" && timerState != TimerState.STARTED) {
            Text(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), text = "Cancel")
        }
        Button(startButtonClick,   colors = ButtonDefaults.buttonColors(
            containerColor = if (timerState == TimerState.STARTED) Red
            else MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        )) {
            Text(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), text = when(timerState) {
                TimerState.STARTED -> "Stop"
                TimerState.STOPPED -> "Resume"
                else -> "Start"
            })
        }
        Button(
            onClick = finishButtonClick,
            enabled = seconds != "00" && timerState != TimerState.STARTED
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Finish"
            )
        }
    }
}

private fun timerTextAnimation(duration: Int = 600): ContentTransform {
    return slideInVertically(animationSpec = tween(duration)) { fullHeight -> fullHeight } +
            fadeIn(animationSpec = tween(duration)) togetherWith
            slideOutVertically(animationSpec = tween(duration)) { fullHeight -> -fullHeight } +
            fadeOut(animationSpec = tween(duration))
}