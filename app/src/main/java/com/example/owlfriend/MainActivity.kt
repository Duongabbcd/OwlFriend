package com.example.owlfriend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.owlfriend.domain.Session
import com.example.owlfriend.domain.Subject
import com.example.owlfriend.domain.Task
import com.example.owlfriend.presentation.NavGraph
import com.example.owlfriend.presentation.NavGraphs
import com.example.owlfriend.presentation.theme.OwlFriendTheme
import com.ramcosta.composedestinations.DestinationsNavHost

@OptIn(ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComposeFoundationFlags.isNonComposedClickableEnabled = false

        enableEdgeToEdge()
        setContent {
            OwlFriendTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }


    companion object {
        val subjects = listOf(
            Subject("English", 10f, colors = Subject.subjectCardColors[0], 1),
            Subject("Physics", 10f, colors = Subject.subjectCardColors[1], 2),
            Subject("Maths", 10f, colors = Subject.subjectCardColors[2], 3),
            Subject("Geology", 10f, colors = Subject.subjectCardColors[3], 4),
            Subject("Fine Arts", 10f, colors = Subject.subjectCardColors[4], 5),
        )


        val tasks = listOf(
            Task(title = "Prepare notes", "", 0L, 0, relatedToSubject = "", false, 0, 1),
            Task(title = "Do homework", "", 0L, 1, relatedToSubject = "", true, 0, 1),
            Task(title = "Go coaching", "", 0L, 2, relatedToSubject = "", false, 0, 1),
            Task(title = "Assignments", "", 0L, 0, relatedToSubject = "", false, 0, 1),
            Task(title = "Write poem", "", 0L, 1, relatedToSubject = "", false, 0, 1)
        )

        val sessions = listOf(
            Session(
                relatedToSubject = "English",
                date = 0L,
                duration = 0.5f,
                sessionSubjectId = 0,
                sessionId = 0
            ),
            Session(
                relatedToSubject = "Physics",
                date = 0L,
                duration = 2f,
                sessionSubjectId = 0,
                sessionId = 0
            ),
            Session(
                relatedToSubject = "Maths",
                date = 0L,
                duration = 1f,
                sessionSubjectId = 0,
                sessionId = 0
            ),
            Session(
                relatedToSubject = "English",
                date = 0L,
                duration = 0f,
                sessionSubjectId = 0,
                sessionId = 0
            ),
            Session(
                relatedToSubject = "English",
                date = 0L,
                duration = 1.25f,
                sessionSubjectId = 0,
                sessionId = 0
            ),
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OwlFriendTheme {
        Greeting("Android")
    }
}
