package alessandro.vendramini.hackernews.presentation.navigations

import alessandro.vendramini.hackernews.presentation.views.HackerNewsWebView
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
}

object InCommonGraph {
    const val WEB_VIEW = "web_view"
}
