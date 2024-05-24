package alessandro.vendramini.hackernews.presentation.viewmodels.events

sealed interface DashboardViewModelEvent {
    data class UpdateSelectedDestinationState(val selectedDestination: String) : DashboardViewModelEvent
    data object FetchNewStoriesIds : DashboardViewModelEvent
    data object FetchTopStoriesIds : DashboardViewModelEvent
    data object FetchBestStoriesIds : DashboardViewModelEvent
}
