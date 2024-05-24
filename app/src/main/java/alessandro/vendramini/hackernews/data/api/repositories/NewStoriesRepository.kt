package alessandro.vendramini.hackernews.data.api.repositories

import alessandro.vendramini.hackernews.data.api.ApiResource
import alessandro.vendramini.hackernews.data.api.services.NewStoriesService

class NewStoriesRepository(private val service: NewStoriesService) {
    suspend fun getNewStoriesIds(
        onCallbackResource: (ApiResource<List<Long>>) -> Unit,
    ) {
        runCatching {
            service.getNewStoriesIds()
        }.onSuccess { newStoriesIds ->
            onCallbackResource(ApiResource.Success(data = newStoriesIds))
        }.onFailure { exception ->
            onCallbackResource(ApiResource.GeneralError(exception = Exception(exception)))
        }
    }
}
