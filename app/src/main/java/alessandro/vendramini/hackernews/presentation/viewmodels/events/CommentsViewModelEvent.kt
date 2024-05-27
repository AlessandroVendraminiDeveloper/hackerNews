package alessandro.vendramini.hackernews.presentation.viewmodels.events

sealed interface CommentsViewModelEvent {
    data class FetchCommentsByIds(val listOfIds: List<Long>) : CommentsViewModelEvent
}
