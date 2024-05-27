package alessandro.vendramini.hackernews.presentation.viewmodels.states

import alessandro.vendramini.hackernews.data.models.StoryModel
import alessandro.vendramini.hackernews.util.PaginationState

data class NewStoriesViewModelState(
    val newStoriesIds: List<Long>? = null,
    val newStories: List<StoryModel>? = null,
    val paginationState: PaginationState = PaginationState(),
    val isRefreshing: Boolean = false,
)
