package alessandro.vendramini.hackernews.data.api.repositories

import alessandro.vendramini.hackernews.data.api.ApiResource
import alessandro.vendramini.hackernews.data.api.services.StoriesService
import alessandro.vendramini.hackernews.data.api.services.StoryDetailService
import alessandro.vendramini.hackernews.data.models.StoryModel

class StoriesRepository(
    private val storiesService: StoriesService,
    private val storyDetailService: StoryDetailService,
) {
    suspend fun getNewStoriesIds(
        onCallbackResource: (ApiResource<List<Long>>) -> Unit,
    ) {
        runCatching {
            storiesService.getNewStoriesIds()
        }.onSuccess { newStoriesIds ->
            onCallbackResource(ApiResource.Success(data = newStoriesIds))
        }.onFailure { exception ->
            onCallbackResource(ApiResource.GeneralError(exception = Exception(exception)))
        }
    }

    suspend fun getTopStoriesIds(
        onCallbackResource: (ApiResource<List<Long>>) -> Unit,
    ) {
        runCatching {
            storiesService.getTopStoriesIds()
        }.onSuccess { topStoriesIds ->
            onCallbackResource(ApiResource.Success(data = topStoriesIds))
        }.onFailure { exception ->
            onCallbackResource(ApiResource.GeneralError(exception = Exception(exception)))
        }
    }

    suspend fun getBestStoriesIds(
        onCallbackResource: (ApiResource<List<Long>>) -> Unit,
    ) {
        runCatching {
            storiesService.getBestStoriesIds()
        }.onSuccess { bestStoriesIds ->
            onCallbackResource(ApiResource.Success(data = bestStoriesIds))
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
