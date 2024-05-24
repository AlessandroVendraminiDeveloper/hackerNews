package alessandro.vendramini.hackernews.presentation.view

import alessandro.vendramini.hackernews.presentation.ui.theme.HackerNewsTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun NewStoriesView(
    navController: NavController,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            Text(text = "New Stories")
        }
    }
}

/**
 * Preview
 */

@Preview
@Composable
private fun LightPreview() {
    HackerNewsTheme(darkTheme = false) {
        NewStoriesView(
            navController = rememberNavController(),
        )
    }
}

@Preview
@Composable
private fun DarkPreview() {
    HackerNewsTheme(darkTheme = true) {
        NewStoriesView(
            navController = rememberNavController(),
        )
    }
}
