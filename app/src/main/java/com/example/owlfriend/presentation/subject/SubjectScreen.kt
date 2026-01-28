package com.example.owlfriend.presentation.subject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.owlfriend.MainActivity
import com.example.owlfriend.domain.Subject
import com.example.owlfriend.presentation.components.AddSubjectDialog
import com.example.owlfriend.presentation.components.CountCard
import com.example.owlfriend.presentation.components.DeleteDialog
import com.example.owlfriend.presentation.components.studySessionsList
import com.example.owlfriend.presentation.components.tasksList
import com.example.owlfriend.presentation.destinations.TaskScreenRouteDestination
import com.example.owlfriend.presentation.session.SessionScreenViewModel
import com.example.owlfriend.presentation.task.TaskScreenNavArgs
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

data class SubjectScreenNavArgs(
    val subjectId: Int
)

@Destination(navArgsDelegate = SubjectScreenNavArgs::class)
@Composable
fun SubjectScreenRoute(
    navigator: DestinationsNavigator
) {
    val viewModel: SubjectViewModel = hiltViewModel()


    SubjectScreen(
        onBackButtonClick = {navigator.navigateUp()},
        onAddTaskButtonClick =  {
            val navArgs = TaskScreenNavArgs(taskId = null, -1)
            navigator.navigate(TaskScreenRouteDestination(navArgs))
        },
        onTaskCardClick = { taskId ->
            val navArgs = TaskScreenNavArgs(taskId = taskId, null)
            navigator.navigate(TaskScreenRouteDestination(navArgs))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreen(
    onBackButtonClick: () -> Unit,
    onAddTaskButtonClick: () -> Unit,
    onTaskCardClick: (Int?) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()
    val isFABExpanded by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }
    var subjectName by remember { mutableStateOf("") }
    var goalHours by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Subject.subjectCardColors.random()) }

    AddSubjectDialog(
        isAddSubjectDialogOpen,
        subjectName = subjectName,
        goalHours = goalHours,
        onSubjectNameChange = { subjectName = it },
        onGoalHoursChange = { goalHours = it },
        selectedColors = selectedColor,
        onColorChange = { selectedColor = it },
        onDismissRequest = { isAddSubjectDialogOpen = false },
        onConfirmButtonClick = {
            isAddSubjectDialogOpen = false
        }
    )

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Subject?",
        bodyText = "Are you sure you want to delete this subject? All related " +
                "tasks and study sessions will be permanetnly removed. This action can not be undone.",
        onDismissRequest = { isDeleteSessionDialogOpen = false },
        onConfirmButtonClick = {
            isDeleteSessionDialogOpen = false
        }
    )


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SubjectScreenTopBar(
                title = "English",
                onBackButtonClick = onBackButtonClick,
                onDeleteButtonClick = { /*TODO*/ },
                onEditButtonClick = {},
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTaskButtonClick,
                icon = { Icon(imageVector = Icons.Default.Add, "Add") },
                text = { Text("Add Task") },
                expanded = isFABExpanded
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SubjectOverviewSection(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    "10", "15", 0.75f
                )
            }
            tasksList(
                "UPCOMING TASKS",
                emptyListText = "You don't have any upcoming tasks.\n " +
                        "Click the + button to add new task.",
                tasks = MainActivity.tasks,
                onCheckBoxClick = {},
                onTaskCardClick = onTaskCardClick
            )

            item {
                Spacer(Modifier.height(20.dp))
            }

            tasksList(
                "COMPLETED TASKS",
                emptyListText = "You don't have any completed tasks.\n " +
                        "Click the check box on completion of task.",
                tasks = MainActivity.tasks,
                onCheckBoxClick = {},
                onTaskCardClick = {}
            )


            studySessionsList(
                "RECENT STUDY SESSIONS",
                emptyListText = "You don't have any recent study sessions\n " +
                        "Start a study session to begin recording your progress",
                sessions = MainActivity.sessions,
                onDeleteIconClick = { isDeleteSessionDialogOpen = true }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreenTopBar(
    title: String,
    onBackButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "navigate back"
                )
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            IconButton(onClick = onDeleteButtonClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Subject"
                )
            }
            IconButton(onClick = onEditButtonClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Subject"
                )
            }
        }
    )
}

@Composable
private fun SubjectOverviewSection(
    modifier: Modifier,
    studiedHours: String,
    goalHours: String,
    progress: Float,
) {
    val percentageProgress = remember(key1 = progress) {
        (progress * 100).toInt().coerceIn(0, 100)
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Study Hours",
            count = goalHours
        )
        Spacer(Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Studied Hours",
            count = studiedHours
        )
        Spacer(Modifier.width(10.dp))
        Box(Modifier.size(75.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = 1f,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = progress,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
            )
            Text("$percentageProgress%")
        }
    }
}