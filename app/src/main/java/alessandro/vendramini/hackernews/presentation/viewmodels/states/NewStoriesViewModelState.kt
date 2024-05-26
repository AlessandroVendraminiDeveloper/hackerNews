package alessandro.vendramini.hackernews.presentation.viewmodels.states

import alessandro.vendramini.hackernews.data.models.StoryModel

data class NewStoriesViewModelState(
    val newStoriesIds: List<Long>? = null,
    val newStories: List<StoryModel>? = null,
    val isRefreshing: Boolean = false,
)
