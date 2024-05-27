package alessandro.vendramini.hackernews.data.api.repositories

import alessandro.vendramini.hackernews.data.api.ApiResource
import alessandro.vendramini.hackernews.data.api.services.ItemDetailService
import alessandro.vendramini.hackernews.data.models.CommentModel

class CommentsRepository(
    private val itemDetailService: ItemDetailService,
) {
    suspend fun getCommentDetail(
        id: Long,
        onCallbackResource: (ApiResource<CommentModel>) -> Unit,
    ) {
        runCatching {
            itemDetailService.getItemDetail(id = id)
        }.onSuccess { item ->
            onCallbackResource(ApiResource.Success(data = item.convertToCommentModel()))
        }.onFailure { exception ->
            onCallbackResource(ApiResource.GeneralError(exception = Exception(exception)))
        }
    }
}
