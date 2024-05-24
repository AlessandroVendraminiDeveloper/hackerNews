package alessandro.vendramini.hackernews.presentation.viewmodels.states

import alessandro.vendramini.hackernews.data.models.StoryModel

data class NewStoriesViewModelState(
    val newStories: List<StoryModel>? = null,
)
