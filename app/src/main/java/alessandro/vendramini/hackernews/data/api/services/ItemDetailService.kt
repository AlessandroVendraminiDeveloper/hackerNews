package alessandro.vendramini.hackernews.data.api.services

import alessandro.vendramini.hackernews.data.models.ItemModel
import alessandro.vendramini.hackernews.data.models.StoryModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ItemDetailService(private val ktor: HttpClient) {
    suspend fun getItemDetail(id: Long): ItemModel = ktor.get(
        urlString = "v0/item/$id.json",
        block = {},
    ).body()
}
