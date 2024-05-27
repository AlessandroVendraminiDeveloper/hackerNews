package alessandro.vendramini.hackernews.presentation.viewmodels.states

import alessandro.vendramini.hackernews.data.models.CommentModel
import alessandro.vendramini.hackernews.util.PaginationState

data class CommentsViewModelState(
    val comments: List<CommentModel>? = null,
    val paginationState: PaginationState = PaginationState(),
)
