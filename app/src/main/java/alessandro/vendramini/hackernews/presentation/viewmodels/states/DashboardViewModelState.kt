package alessandro.vendramini.hackernews.presentation.viewmodels.states

import alessandro.vendramini.hackernews.presentation.navigations.DashboardNavigationRoute

data class DashboardViewModelState(
    val selectedDestination: String = DashboardNavigationRoute.NEW_STORIES,
    val isNavigationBarVisible: Boolean = false,
)
