package alessandro.vendramini.hackernews.presentation.viewmodels.events

sealed interface DashboardViewModelEvent {
    data class UpdateSelectedDestinationState(val selectedDestination: String) : DashboardViewModelEvent
    data class UpdateIsNavigationBarVisibleState(val isVisible: Boolean) : DashboardViewModelEvent
    data object FetchPreferredStoriesIds : DashboardViewModelEvent
}
