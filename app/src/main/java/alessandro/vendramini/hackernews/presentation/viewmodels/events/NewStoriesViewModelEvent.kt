package alessandro.vendramini.hackernews.presentation.viewmodels.events

sealed interface NewStoriesViewModelEvent {
    data class FetchNewStoriesIds(val isRefreshing: Boolean) : NewStoriesViewModelEvent
    data class FetchStoriesByIds(val listOfIds: List<Long>) : NewStoriesViewModelEvent
}
