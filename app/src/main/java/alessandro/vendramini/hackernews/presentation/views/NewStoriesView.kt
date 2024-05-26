package alessandro.vendramini.hackernews.presentation.views

import alessandro.vendramini.hackernews.presentation.components.HackerNewsTopAppBar
import alessandro.vendramini.hackernews.presentation.components.PullToRefreshLazyColumn
import alessandro.vendramini.hackernews.presentation.components.StoryCard
import alessandro.vendramini.hackernews.presentation.ui.theme.HackerNewsTheme
import alessandro.vendramini.hackernews.presentation.viewmodels.events.NewStoriesViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.NewStoriesViewModelState
import alessandro.vendramini.hackernews.util.PaginationState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
    paginationState: PaginationState,
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
                    PullToRefreshLazyColumn(
                        items = uiState.newStories,
                        content = { story ->
                            StoryCard(
                                storyModel = story,
                                onCardClick = {}
                            )
                        },
                        isRefreshing = false,
                        paginationState = paginationState,
                        onRefresh = {
                            /*
                            scope.launch {
                                isRefreshing = true
                                delay(3000L) // Simulated API call
                                isRefreshing = false
                            }

                             */
                        },
                        onCanScrollForward = {
                            onEvent(
                                NewStoriesViewModelEvent.FetchStoriesByIds(
                                    listOfIds = uiState.newStoriesIds!!
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    if (uiState.newStoriesIds == null) {
        LaunchedEffect(
            key1 = true,
            block = {
                onEvent(
                    NewStoriesViewModelEvent.FetchNewStoriesIds
                )
            }
        )
    } else {
        LaunchedEffect(
            key1 = uiState.newStoriesIds,
            block = {
                onEvent(
                    NewStoriesViewModelEvent.FetchStoriesByIds(
                        listOfIds = uiState.newStoriesIds,
                    )
                )
            }
        )
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
            uiState = NewStoriesViewModelState(),
            paginationState = PaginationState(),
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
            paginationState = PaginationState(),
            onEvent = {},
        )
    }
}
