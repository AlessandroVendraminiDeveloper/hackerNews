package alessandro.vendramini.hackernews.presentation.viewmodel.event

sealed interface DashboardViewModelEvent {
    data class UpdateSelectedDestinationState(val selectedDestination: String) : DashboardViewModelEvent
}
