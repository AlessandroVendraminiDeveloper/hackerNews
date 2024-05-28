package alessandro.vendramini.hackernews.presentation.viewmodels.events

sealed interface TopStoriesViewModelEvent {
    data class FetchTopStoriesIds(val isRefreshing: Boolean) : TopStoriesViewModelEvent
    data class FetchStoriesDetailByIds(val listOfIds: List<Long>) : TopStoriesViewModelEvent
    data class UpdatePreferredList(
        val id: Long,
        val isAddFromList: Boolean,
    ) : TopStoriesViewModelEvent
}
