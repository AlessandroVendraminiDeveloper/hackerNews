package alessandro.vendramini.hackernews.presentation.views

import alessandro.vendramini.hackernews.presentation.components.HackerNewsTopAppBar
import alessandro.vendramini.hackernews.presentation.components.PullToRefreshLazyColumn
import alessandro.vendramini.hackernews.presentation.components.StoryCard
import alessandro.vendramini.hackernews.presentation.components.skeleton.StorySkeletonCard
import alessandro.vendramini.hackernews.presentation.navigations.InCommonGraph
import alessandro.vendramini.hackernews.presentation.ui.theme.HackerNewsTheme
import alessandro.vendramini.hackernews.presentation.viewmodels.DashboardViewModel
import alessandro.vendramini.hackernews.presentation.viewmodels.events.BestStoriesViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.BestStoriesViewModelState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun BestStoriesView(
    navController: NavController,
    uiState: BestStoriesViewModelState,
    onEvent: (BestStoriesViewModelEvent) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HackerNewsTopAppBar(
                title = "Best Stories",
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                uiState.bestStories == null -> {
                    // Is loading
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(count = 10) {
                            StorySkeletonCard()
                        }
                    }
                }
                uiState.bestStories.isEmpty()-> {
                    // Show placeholder
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(text = "List is empty, try to refresh")
                    }
                }
                else -> {
                    PullToRefreshLazyColumn(
                        items = uiState.bestStories,
                        content = { story ->
                            StoryCard(
                                storyModel = story,
                                isPreferred = DashboardViewModel.preferredIds.contains(story.id),
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
                                },
                                onLikeClick = {
                                    onEvent(
                                        BestStoriesViewModelEvent.UpdatePreferredList(
                                            id = story.id,
                                            isAddFromList = !DashboardViewModel.preferredIds.contains(story.id),
                                        )
                                    )
                                },
                                onCommentsClick = {
                                    navController.navigate(
                                        route = "${InCommonGraph.COMMENTS}/${story.kids}",
                                    )
                                },
                            )
                        },
                        isRefreshing = uiState.isRefreshing,
                        paginationState = uiState.paginationState,
                        onRefresh = {
                            onEvent(
                                BestStoriesViewModelEvent.FetchBestStoriesIds(isRefreshing = true)
                            )
                        },
                        onCanScrollForward = {
                            uiState.bestStoriesIds?.let { ids ->
                                BestStoriesViewModelEvent.FetchStoriesByIds(
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
        uiState.bestStoriesIds == null -> {
            LaunchedEffect(
                key1 = true,
                block = {
                    onEvent(
                        BestStoriesViewModelEvent.FetchBestStoriesIds(isRefreshing = false)
                    )
                }
            )
        }
        uiState.paginationState.page == 0 && !uiState.isRefreshing -> {
            LaunchedEffect(
                key1 = true,
                block = {
                    onEvent(
                        BestStoriesViewModelEvent.FetchStoriesByIds(
                            listOfIds = uiState.bestStoriesIds,
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
        BestStoriesView(
            navController = rememberNavController(),
            uiState = BestStoriesViewModelState(),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun DarkPreview() {
    HackerNewsTheme(darkTheme = true) {
        BestStoriesView(
            navController = rememberNavController(),
            uiState = BestStoriesViewModelState(),
            onEvent = {},
        )
    }
}
