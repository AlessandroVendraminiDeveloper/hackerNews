package alessandro.vendramini.hackernews.presentation.viewmodel.state

import alessandro.vendramini.hackernews.presentation.navigation.DashboardNavigationRoute

data class DashboardViewModelState(
    val selectedDestination: String = DashboardNavigationRoute.NEW_STORIES,
)
