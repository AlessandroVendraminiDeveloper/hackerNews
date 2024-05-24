package alessandro.vendramini.hackernews.presentation.views

import alessandro.vendramini.hackernews.presentation.components.HackerNewsNavigationBar
import alessandro.vendramini.hackernews.presentation.navigations.DashboardNavGraph
import alessandro.vendramini.hackernews.presentation.navigations.DashboardNavigationActions
import alessandro.vendramini.hackernews.presentation.navigations.DashboardNavigationRoute
import alessandro.vendramini.hackernews.presentation.viewmodels.events.DashboardViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.DashboardViewModelState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun DashboardView(
    navController: NavHostController,
    uiState: DashboardViewModelState,
    onEvent: (DashboardViewModelEvent) -> Unit,
) {
    val navigationActions = remember(navController) { DashboardNavigationActions(navController) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            HackerNewsNavigationBar(
                selectedDestination = uiState.selectedDestination,
                navigationActions = navigationActions::navigateTo,
            )
        },
    ) { _ ->
        DashboardNavGraph(navHostController = navController)
    }

    LaunchedEffect(
        key1 = navBackStackEntry?.destination,
        block = {
            onEvent(
                DashboardViewModelEvent.UpdateSelectedDestinationState(
                    selectedDestination = navBackStackEntry?.destination?.route
                        ?: DashboardNavigationRoute.TOP_STORIES,
                ),
            )
        },
    )
}