package alessandro.vendramini.hackernews.presentation.viewmodels.states

import alessandro.vendramini.hackernews.data.models.StoryModel
import alessandro.vendramini.hackernews.util.PaginationState

data class BestStoriesViewModelState(
    val bestStoriesIds: List<Long>? = null,
    val bestStories: List<StoryModel>? = null,
    val paginationState: PaginationState = PaginationState(),
    val isRefreshing: Boolean = false,
)
