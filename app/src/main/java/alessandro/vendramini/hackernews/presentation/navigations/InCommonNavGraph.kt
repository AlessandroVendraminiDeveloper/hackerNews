package alessandro.vendramini.hackernews.presentation.navigations

import alessandro.vendramini.hackernews.presentation.viewmodels.CommentsViewModel
import alessandro.vendramini.hackernews.presentation.views.CommentsView
import alessandro.vendramini.hackernews.presentation.views.HackerNewsWebView
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import java.net.URLDecoder

fun NavGraphBuilder.inCommonNavGraph(navHostController: NavHostController) {
    composable(
        route = "${InCommonGraph.WEB_VIEW}/{title}/{url}",
        arguments = listOf(
            navArgument(name = "title") {
                type = NavType.StringType
            },
            navArgument(name = "url") {
                type = NavType.StringType
            },
        ),
    ) { navBackStack ->
        val title = navBackStack.arguments?.getString("title")
        val url = navBackStack.arguments?.getString("url")
        val decodedUrl = URLDecoder.decode(url, "UTF-8")

        HackerNewsWebView(
            navController = navHostController,
            title = title,
            url = decodedUrl,
        )
    }
    composable(
        route = "${InCommonGraph.COMMENTS}/{listOfIds}",
        arguments = listOf(
            navArgument(name = "listOfIds") {
                type = NavType.StringType
            },
        ),
    ) { navBackStack ->
        val listOfIds = navBackStack.arguments?.getString("listOfIds")
        val trimmedInput = listOfIds?.removeSurrounding("[", "]")?.trim()

        val listOfLong = if (trimmedInput.isNullOrEmpty()) {
            emptyList()
        } else {
            trimmedInput.split(",")
                .map { it.trim().toLong() }
        }

        val viewModel = koinViewModel<CommentsViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        CommentsView(
            navController = navHostController,
            listOfIds = listOfLong,
            uiState = uiState,
            onEvent = viewModel::onEvent,
        )
    }
}

object InCommonGraph {
    const val WEB_VIEW = "web_view"
    const val COMMENTS = "comments"
}
