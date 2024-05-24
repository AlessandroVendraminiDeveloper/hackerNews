package alessandro.vendramini.hackernews.presentation.navigations

import alessandro.vendramini.hackernews.presentation.views.BestStoriesView
import alessandro.vendramini.hackernews.presentation.views.NewStoriesView
import alessandro.vendramini.hackernews.presentation.views.TopStoriesView
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun DashboardNavGraph(navHostController: NavHostController) {
    NavHost(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        navController = navHostController,
        route = "dashboard",
        startDestination = DashboardNavigationRoute.NEW_STORIES,
        enterTransition = { fadeIn(animationSpec = tween(durationMillis = 400)) },
        exitTransition = { fadeOut(animationSpec = tween(durationMillis = 400)) },
    ) {
        /**
         * Tabs views
         */
        composable(
            route = DashboardNavigationRoute.NEW_STORIES
        ) {
            NewStoriesView(navController = navHostController)
        }
        composable(
            route = DashboardNavigationRoute.TOP_STORIES,
        ) {
            TopStoriesView(navController = navHostController)
        }
        composable(
            route = DashboardNavigationRoute.BEST_STORIES,
        ) {
            BestStoriesView(navController = navHostController)
        }
    }
}
