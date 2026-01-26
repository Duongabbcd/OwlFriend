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
import com.example.owlfriend.presentation.dashboard.DashboardScreen
import com.example.owlfriend.presentation.theme.OwlFriendTheme

@OptIn(ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComposeFoundationFlags.isNonComposedClickableEnabled = false

        enableEdgeToEdge()
        setContent {
            OwlFriendTheme {
                DashboardScreen()
            }
        }
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