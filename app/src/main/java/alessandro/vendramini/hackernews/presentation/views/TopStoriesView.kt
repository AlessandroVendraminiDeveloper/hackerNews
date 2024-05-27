package alessandro.vendramini.hackernews.presentation.views

import alessandro.vendramini.hackernews.presentation.components.HackerNewsTopAppBar
import alessandro.vendramini.hackernews.presentation.components.PullToRefreshLazyColumn
import alessandro.vendramini.hackernews.presentation.components.StoryCard
import alessandro.vendramini.hackernews.presentation.navigations.InCommonGraph
import alessandro.vendramini.hackernews.presentation.ui.theme.HackerNewsTheme
import alessandro.vendramini.hackernews.presentation.viewmodels.events.TopStoriesViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.TopStoriesViewModelState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun TopStoriesView(
    navController: NavController,
    uiState: TopStoriesViewModelState,
    onEvent: (TopStoriesViewModelEvent) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HackerNewsTopAppBar(
                title = "Top Stories",
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                uiState.topStories == null -> {
                    // Is loading
                }
                uiState.topStories.isEmpty()-> {
                    // Show placeholder
                }
                else -> {
                    PullToRefreshLazyColumn(
                        items = uiState.topStories,
                        content = { story ->
                            StoryCard(
                                storyModel = story,
                                onCardClick = {
                                    coroutineScope.launch {
                                        val url = withContext(Dispatchers.IO) {
                                            URLEncoder.encode(
                                                story.url,
                                                StandardCharsets.UTF_8.toString(),
                                            )
                                        }
                                        val title = story.title ?: "-"
                                        navController.navigate(
                                            route = "${InCommonGraph.WEB_VIEW}/$title/$url",
                                        )
                                    }
                                }
                            )
                        },
                        isRefreshing = uiState.isRefreshing,
                        paginationState = uiState.paginationState,
                        onRefresh = {
                            onEvent(
                                TopStoriesViewModelEvent.FetchTopStoriesIds(isRefreshing = true)
                            )
                        },
                        onCanScrollForward = {
                            uiState.topStoriesIds?.let { ids ->
                                TopStoriesViewModelEvent.FetchStoriesByIds(
                                    listOfIds = ids,
                                )
                            }?.let {
                                onEvent(it)
                            }
                        }
                    )
                }
            }
        }
    }

    when {
        uiState.topStoriesIds == null -> {
            LaunchedEffect(
                key1 = true,
                block = {
                    onEvent(
                        TopStoriesViewModelEvent.FetchTopStoriesIds(isRefreshing = false)
                    )
                }
            )
        }
        uiState.paginationState.page == 0 -> {
            LaunchedEffect(
                key1 = true,
                block = {
                    onEvent(
                        TopStoriesViewModelEvent.FetchStoriesByIds(
                            listOfIds = uiState.topStoriesIds,
                        )
                    )
                }
            )
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
        TopStoriesView(
            navController = rememberNavController(),
            uiState = TopStoriesViewModelState(),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun DarkPreview() {
    HackerNewsTheme(darkTheme = true) {
        TopStoriesView(
            navController = rememberNavController(),
            uiState = TopStoriesViewModelState(),
            onEvent = {},
        )
    }
}
