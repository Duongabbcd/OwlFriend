package com.example.owlfriend.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.owlfriend.R
import com.example.owlfriend.domain.Session
import com.example.owlfriend.domain.Subject
import com.example.owlfriend.domain.Task
import com.example.owlfriend.presentation.components.CountCard
import com.example.owlfriend.presentation.components.SubjectCard
import com.example.owlfriend.presentation.components.studySessionsList
import com.example.owlfriend.presentation.components.tasksList


@Composable
fun DashboardScreen() {
    val subjects = listOf(
        Subject("English", 10f, colors = Subject.subjectCardColors[0]),
        Subject("Physics", 10f, colors = Subject.subjectCardColors[1]),
        Subject("Maths", 10f, colors = Subject.subjectCardColors[2]),
        Subject("Geology", 10f, colors = Subject.subjectCardColors[3]),
        Subject("Fine Arts", 10f, colors = Subject.subjectCardColors[4]),
    )


    val tasks = listOf(
        Task(title = "Prepare notes", "", 0L, 0, relatedToSubject = "", false,0, 1),
        Task(title = "Do homework", "", 0L, 1, relatedToSubject = "", true, 0,1 ),
        Task(title = "Go coaching", "", 0L, 2, relatedToSubject = "", false, 0, 1),
        Task(title = "Assignments", "", 0L, 0, relatedToSubject = "", false, 0, 1),
        Task(title = "Write poem", "", 0L, 1, relatedToSubject = "", false, 0, 1)
    )

    val sessions = listOf(
        Session(relatedToSubject = "English", date = 0L, duration = 0.5f, sessionSubjectId = 0, sessionId = 0),
        Session(relatedToSubject = "Physics", date = 0L, duration = 2f, sessionSubjectId = 0, sessionId = 0),
        Session(relatedToSubject = "Maths", date = 0L, duration = 1f, sessionSubjectId = 0, sessionId = 0),
        Session(relatedToSubject = "English", date = 0L, duration = 0f, sessionSubjectId = 0, sessionId = 0),
        Session(relatedToSubject = "English", date = 0L, duration = 1.25f, sessionSubjectId = 0, sessionId = 0),
    )
    Scaffold(topBar = { DashboardScreenTopBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            item {
                CountCardsSection(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjectCount = 5,
                    studiedHours = "10",
                    goalHours = "15"
                )
            }
            item {
                SubjectCardSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = subjects
                )
            }
            item {
                Button(
                    onClick = { /*TODO*/},
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp, vertical = 20.dp)
                ) {
                    Text(text = "Start Study Session")
                }
            }

            tasksList(
                "UPCOMING TASKS",
                emptyListText = "You don't have any upcoming tasks.\n " +
                        "Click the + button in subject screen to add new task.",
                tasks = tasks,
                onCheckBoxClick = {},
                onTaskCardClick = {}
            )

            studySessionsList(
                "RECENT STUDY SESSIONS",
                emptyListText = "You don't have any recent study sessions\n " +
                        "Start a study session to begin recording your progress",
                sessions = sessions,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "StudySmart",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}

@Composable
private fun CountCardsSection(
    modifier: Modifier, subjectCount: Int, studiedHours: String, goalHours: String
) {
    Row(modifier = modifier) {
        CountCard(Modifier.weight(1f), headingText = "Subject Count", count = "$subjectCount")
        Spacer(Modifier.width(10.dp))
        CountCard(Modifier.weight(1f), headingText = "Studied Hours", count = studiedHours)
        Spacer(Modifier.width(10.dp))
        CountCard(Modifier.weight(1f), headingText = "Goal Study Hours", count = goalHours)
    }
}

@Composable
private fun SubjectCardSection(
    modifier: Modifier,
    subjectList: List<Subject>,
    emptyListText: String = "You don't have any subject.\n Click the + button to add new subjects"
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SUBJECTS",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp)
            )
            IconButton(onClick = {
                //TODO
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Subject")
            }
        }

        if (subjectList.isEmpty()) {
            Image(
                modifier = Modifier.size(120.dp),
                painter = painterResource(R.drawable.img_books),
                contentDescription = emptyListText
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }


        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(subjectList) { subject ->
                SubjectCard(
                    subjectName = subject.name ,
                    gradientColors = subject.colors,
                    onClick = {}
                )

            }
        }
    }
}