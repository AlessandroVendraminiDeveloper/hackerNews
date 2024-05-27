package alessandro.vendramini.hackernews.presentation.viewmodels.states

import alessandro.vendramini.hackernews.data.models.CommentModel

data class CommentsViewModelState(
    val comments: List<CommentModel>? = null,
)
