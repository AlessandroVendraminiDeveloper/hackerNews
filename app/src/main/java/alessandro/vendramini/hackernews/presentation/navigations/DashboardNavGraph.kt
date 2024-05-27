package alessandro.vendramini.hackernews.presentation.navigations

import alessandro.vendramini.hackernews.presentation.viewmodels.BestStoriesViewModel
import alessandro.vendramini.hackernews.presentation.viewmodels.NewStoriesViewModel
import alessandro.vendramini.hackernews.presentation.viewmodels.TopStoriesViewModel
import alessandro.vendramini.hackernews.presentation.views.BestStoriesView
import alessandro.vendramini.hackernews.presentation.views.NewStoriesView
import alessandro.vendramini.hackernews.presentation.views.TopStoriesView
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardNavGraph(
    navHostController: NavHostController,
    paddingValues: PaddingValues,
) {
    NavHost(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues = paddingValues),
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
            val viewModel = koinViewModel<NewStoriesViewModel>()
            val uiState by viewModel.uiState.collectAsState()

            NewStoriesView(
                navController = navHostController,
                uiState = uiState,
                onEvent = viewModel::onEvent,
            )
        }
        composable(
            route = DashboardNavigationRoute.TOP_STORIES,
        ) {
            val viewModel = koinViewModel<TopStoriesViewModel>()
            val uiState by viewModel.uiState.collectAsState()

            TopStoriesView(
                navController = navHostController,
                uiState = uiState,
                onEvent = viewModel::onEvent,
            )
        }
        composable(
            route = DashboardNavigationRoute.BEST_STORIES,
        ) {
            val viewModel = koinViewModel<BestStoriesViewModel>()
            val uiState by viewModel.uiState.collectAsState()

            BestStoriesView(
                navController = navHostController,
                uiState = uiState,
                onEvent = viewModel::onEvent,
            )
        }

        /**
         * InCommon views
         */
        inCommonNavGraph(navHostController = navHostController)
    }
}
