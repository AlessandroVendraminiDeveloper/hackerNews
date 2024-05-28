package alessandro.vendramini.hackernews.presentation.viewmodels.events

sealed interface CommentsViewModelEvent {
    data class FetchCommentsDetailByIds(val listOfIds: List<Long>) : CommentsViewModelEvent
    data class FetchAnswersByParents(
        val parent: Long,
        val listOfIds: List<Long>,
    ) : CommentsViewModelEvent
}
