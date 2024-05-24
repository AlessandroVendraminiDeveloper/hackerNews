package alessandro.vendramini.hackernews.presentation.view

import alessandro.vendramini.hackernews.presentation.component.HackerNewsNavigationBar
import alessandro.vendramini.hackernews.presentation.navigation.DashboardNavGraph
import alessandro.vendramini.hackernews.presentation.navigation.DashboardNavigationActions
import alessandro.vendramini.hackernews.presentation.navigation.DashboardNavigationRoute
import alessandro.vendramini.hackernews.presentation.viewmodel.event.DashboardViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodel.state.DashboardViewModelState
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