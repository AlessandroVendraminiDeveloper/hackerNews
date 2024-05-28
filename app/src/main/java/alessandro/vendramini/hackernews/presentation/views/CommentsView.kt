package alessandro.vendramini.hackernews.presentation.views

import alessandro.vendramini.hackernews.presentation.components.HackerNewsTopAppBar
import alessandro.vendramini.hackernews.presentation.components.card.CommentCard
import alessandro.vendramini.hackernews.presentation.viewmodels.events.CommentsViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.CommentsViewModelState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun CommentsView(
    navController: NavController,
    listOfIds: List<Long>,
    uiState: CommentsViewModelState,
    onEvent: (CommentsViewModelEvent) -> Unit,
) {
    /** View ui **/
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HackerNewsTopAppBar(
                title = "Comments",
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                uiState.comments == null -> {
                    // Is loading
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    }
                }
                uiState.comments.isEmpty() -> {
                    // Show placeholder
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(text = "This story currently has no comments")
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.comments) { comment ->
                            CommentCard(
                                modifier = Modifier.padding(16.dp),
                                commentModel = comment,
                                onShowMoreClick = { pair ->
                                    onEvent(
                                        CommentsViewModelEvent.FetchAnswersByParents(
                                            parent = pair.first,
                                            listOfIds = pair.second,
                                        )
                                    )
                                }
                            )
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                when {
                                    uiState.paginationState.endReached -> {
                                        // not show
                                    }

                                    uiState.paginationState.isLoading -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                        )
                                    }

                                    else -> {
                                        Text(
                                            modifier = Modifier.clickable {
                                                onEvent(
                                                    CommentsViewModelEvent.FetchCommentsDetailByIds(
                                                        listOfIds = listOfIds,
                                                    )
                                                )
                                            },
                                            text = AnnotatedString(
                                                text = "Show more...",
                                                spanStyle = SpanStyle(
                                                    textDecoration = TextDecoration.Underline,
                                                ),
                                            ),
                                            fontWeight = FontWeight.Bold,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /** Actions **/
    if (uiState.paginationState.page == 0) {
        LaunchedEffect(
            key1 = true,
            block = {
                onEvent(
                    CommentsViewModelEvent.FetchCommentsDetailByIds(
                        listOfIds = listOfIds,
                    )
                )
            }
        )
    }
}

@Composable
@Preview
private fun Preview() {
    CommentsView(
        navController = rememberNavController(),
        listOfIds = listOf(),
        uiState = CommentsViewModelState(),
        onEvent = {},
    )
}
