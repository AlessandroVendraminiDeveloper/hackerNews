package alessandro.vendramini.hackernews.presentation.viewmodels.events

sealed interface FavoriteStoriesViewModelEvent {
    data class FetchStoriesDetailByIds(val listOfIds: List<Long>) : FavoriteStoriesViewModelEvent
    data class UpdatePreferredList(
        val id: Long,
        val isAddFromList: Boolean,
    ) : FavoriteStoriesViewModelEvent
}
