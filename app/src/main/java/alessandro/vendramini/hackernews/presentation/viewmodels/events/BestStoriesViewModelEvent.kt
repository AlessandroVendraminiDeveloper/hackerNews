package alessandro.vendramini.hackernews.presentation.viewmodels.events

sealed interface BestStoriesViewModelEvent {
    data class FetchBestStoriesIds(val isRefreshing: Boolean) : BestStoriesViewModelEvent
    data class FetchStoriesDetailByIds(val listOfIds: List<Long>) : BestStoriesViewModelEvent
    data class UpdatePreferredList(
        val id: Long,
        val isAddFromList: Boolean,
    ) : BestStoriesViewModelEvent
}
