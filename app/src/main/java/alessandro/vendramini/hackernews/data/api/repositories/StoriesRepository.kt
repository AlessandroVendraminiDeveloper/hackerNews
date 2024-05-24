package alessandro.vendramini.hackernews.data.api.repositories

import alessandro.vendramini.hackernews.data.api.ApiResource
import alessandro.vendramini.hackernews.data.api.services.NewStoriesService
import alessandro.vendramini.hackernews.data.api.services.StoryDetailService
import alessandro.vendramini.hackernews.data.models.StoryModel

class StoriesRepository(
    private val newStoriesService: NewStoriesService,
    private val storyDetailService: StoryDetailService,
) {
    suspend fun getNewStoriesIds(
        onCallbackResource: (ApiResource<List<Long>>) -> Unit,
    ) {
        runCatching {
            newStoriesService.getNewStoriesIds()
        }.onSuccess { newStoriesIds ->
            onCallbackResource(ApiResource.Success(data = newStoriesIds))
        }.onFailure { exception ->
            onCallbackResource(ApiResource.GeneralError(exception = Exception(exception)))
        }
    }

    suspend fun getStoryDetail(
        id: Long,
        onCallbackResource: (ApiResource<StoryModel>) -> Unit,
    ) {
        runCatching {
            storyDetailService.getStoryDetail(id = id)
        }.onSuccess { story ->
            onCallbackResource(ApiResource.Success(data = story))
        }.onFailure { exception ->
            onCallbackResource(ApiResource.GeneralError(exception = Exception(exception)))
        }
    }
}
