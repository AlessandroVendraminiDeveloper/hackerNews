package alessandro.vendramini.hackernews.presentation.views

import alessandro.vendramini.hackernews.data.models.StoryModel
import alessandro.vendramini.hackernews.presentation.components.HackerNewsTopAppBar
import alessandro.vendramini.hackernews.presentation.components.StoryCard
import alessandro.vendramini.hackernews.presentation.ui.theme.HackerNewsTheme
import alessandro.vendramini.hackernews.presentation.viewmodels.DashboardViewModel
import alessandro.vendramini.hackernews.presentation.viewmodels.events.NewStoriesViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.NewStoriesViewModelState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun NewStoriesView(
    navController: NavController,
    uiState: NewStoriesViewModelState,
    onEvent: (NewStoriesViewModelEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HackerNewsTopAppBar(
                title = "New Stories",
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                uiState.newStories == null -> {
                    // Is loading
                }
                uiState.newStories.isEmpty()-> {
                    // Show placeholder
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(uiState.newStories) { story ->
                            StoryCard(
                                storyModel = story,
                                onCardClick = {}
                            )
                        }
                    }
                }
            }
        }
    }
    
    LaunchedEffect(
        key1 = DashboardViewModel.newStoriesIds,
        block = {
            DashboardViewModel.newStoriesIds?.let {
                NewStoriesViewModelEvent.FetchStoriesByIds(
                    listOfIds = it,
                )
            }?.let {
                onEvent(it)
            }
        }
    )
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
            uiState = NewStoriesViewModelState(),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun DarkPreview() {
    HackerNewsTheme(darkTheme = true) {
        NewStoriesView(
            navController = rememberNavController(),
            uiState = NewStoriesViewModelState(),
            onEvent = {},
        )
    }
}
