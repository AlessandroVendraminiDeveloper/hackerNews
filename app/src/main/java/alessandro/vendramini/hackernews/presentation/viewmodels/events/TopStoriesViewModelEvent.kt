package alessandro.vendramini.hackernews.presentation.viewmodels.events

sealed interface TopStoriesViewModelEvent {
    data class FetchTopStoriesIds(val isRefreshing: Boolean) : TopStoriesViewModelEvent
    data class FetchStoriesByIds(val listOfIds: List<Long>) : TopStoriesViewModelEvent
}
