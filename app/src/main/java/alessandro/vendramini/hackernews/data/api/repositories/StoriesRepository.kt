package alessandro.vendramini.hackernews.data.api.repositories

import alessandro.vendramini.hackernews.data.api.ApiResource
import alessandro.vendramini.hackernews.data.api.services.StoriesService
import alessandro.vendramini.hackernews.data.api.services.ItemDetailService
import alessandro.vendramini.hackernews.data.models.StoryModel

class StoriesRepository(
    private val storiesService: StoriesService,
    private val itemDetailService: ItemDetailService,
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
            itemDetailService.getItemDetail(id = id)
        }.onSuccess { item ->
            onCallbackResource(ApiResource.Success(data = item.convertToStoryModel()))
        }.onFailure { exception ->
            onCallbackResource(ApiResource.GeneralError(exception = Exception(exception)))
        }
    }
}
