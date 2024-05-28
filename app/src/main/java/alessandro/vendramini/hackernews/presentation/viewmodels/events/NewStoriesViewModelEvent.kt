package alessandro.vendramini.hackernews.presentation.viewmodels.events

sealed interface NewStoriesViewModelEvent {
    data class FetchNewStoriesIds(val isRefreshing: Boolean) : NewStoriesViewModelEvent
    data class FetchStoriesDetailByIds(val listOfIds: List<Long>) : NewStoriesViewModelEvent
    data class UpdatePreferredList(
        val id: Long,
        val isAddFromList: Boolean,
    ) : NewStoriesViewModelEvent
}
