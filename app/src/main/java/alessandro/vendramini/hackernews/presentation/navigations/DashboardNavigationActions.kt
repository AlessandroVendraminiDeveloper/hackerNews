package alessandro.vendramini.hackernews.presentation.navigations

import alessandro.vendramini.hackernews.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberNew
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object DashboardNavigationRoute {
    const val NEW_STORIES = "new_stories_tab"
    const val TOP_STORIES = "top_stories_tab"
    const val BEST_STORIES = "best_stories_tab"
    const val FAVORITES = "favorites_tab"
}

data class NavigationBarModel(
    val route: String,
    val titleId: Int,
    val icon: ImageVector,
)

val NAVIGATION_BAR_DESTINATIONS = listOf(
    NavigationBarModel(
        route = DashboardNavigationRoute.NEW_STORIES,
        titleId = R.string.new_stories_title,
        icon = Icons.Default.FiberNew,
    ),
    NavigationBarModel(
        route = DashboardNavigationRoute.TOP_STORIES,
        titleId = R.string.top_stories_title,
        icon = Icons.Default.Highlight,
    ),
    NavigationBarModel(
        route = DashboardNavigationRoute.BEST_STORIES,
        titleId = R.string.best_stories_title,
        icon = Icons.Default.Star,
    ),
    NavigationBarModel(
        route = DashboardNavigationRoute.FAVORITES,
        titleId = R.string.favorites_stories_title,
        icon = Icons.Default.HeartBroken,
    ),
)

class DashboardNavigationActions(private val navController: NavHostController) {
    fun navigateTo(navigationBarModel: NavigationBarModel) {
        navController.navigate(navigationBarModel.route) {
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
        }
    }
}