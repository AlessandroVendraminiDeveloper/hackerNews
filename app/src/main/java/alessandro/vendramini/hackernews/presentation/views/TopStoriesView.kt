package alessandro.vendramini.hackernews.presentation.views

import alessandro.vendramini.hackernews.presentation.components.HackerNewsTopAppBar
import alessandro.vendramini.hackernews.presentation.components.PullToRefreshLazyColumn
import alessandro.vendramini.hackernews.presentation.components.card.StoryCard
import alessandro.vendramini.hackernews.presentation.components.skeleton.StorySkeletonCard
import alessandro.vendramini.hackernews.presentation.navigations.InCommonGraph
import alessandro.vendramini.hackernews.presentation.ui.theme.HackerNewsTheme
import alessandro.vendramini.hackernews.presentation.viewmodels.DashboardViewModel
import alessandro.vendramini.hackernews.presentation.viewmodels.events.TopStoriesViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.TopStoriesViewModelState
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
fun TopStoriesView(
    navController: NavController,
    uiState: TopStoriesViewModelState,
    onEvent: (TopStoriesViewModelEvent) -> Unit,
) {
    /** States **/
    val coroutineScope = rememberCoroutineScope()

    /** View ui **/
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
                uiState.topStories.isEmpty()-> {
                    // Show placeholder
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(text = "List is empty, try to refresh")
                    }
                }
                else -> {
                    PullToRefreshLazyColumn(
                        items = uiState.topStories,
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
                                        TopStoriesViewModelEvent.UpdatePreferredList(
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
                                TopStoriesViewModelEvent.FetchTopStoriesIds(isRefreshing = true)
                            )
                        },
                        onCanScrollForward = {
                            uiState.topStoriesIds?.let { ids ->
                                TopStoriesViewModelEvent.FetchStoriesDetailByIds(
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

    /** Actions **/
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
        uiState.paginationState.page == 0 && !uiState.isRefreshing -> {
            LaunchedEffect(
                key1 = true,
                block = {
                    onEvent(
                        TopStoriesViewModelEvent.FetchStoriesDetailByIds(
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
