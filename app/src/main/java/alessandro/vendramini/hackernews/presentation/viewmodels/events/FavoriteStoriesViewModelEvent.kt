package alessandro.vendramini.hackernews.presentation.viewmodels.events

sealed interface FavoriteStoriesViewModelEvent {
    data class FetchStoriesByIds(val listOfIds: List<Long>) : FavoriteStoriesViewModelEvent
    data class UpdatePreferredList(
        val id: Long,
        val isAddFromList: Boolean,
    ) : FavoriteStoriesViewModelEvent
}
