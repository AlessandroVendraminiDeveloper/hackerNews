package alessandro.vendramini.hackernews.presentation.viewmodels.states

import alessandro.vendramini.hackernews.data.models.StoryModel

data class FavoriteStoriesViewModelState(
    val favoriteStories: List<StoryModel>? = null,
)
