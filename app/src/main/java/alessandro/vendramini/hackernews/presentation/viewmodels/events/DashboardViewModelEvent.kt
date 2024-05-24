package alessandro.vendramini.hackernews.presentation.viewmodels.events

sealed interface DashboardViewModelEvent {
    data class UpdateSelectedDestinationState(val selectedDestination: String) : DashboardViewModelEvent
}
