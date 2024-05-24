package alessandro.vendramini.hackernews.data.api.services

import alessandro.vendramini.hackernews.data.models.StoryModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class StoryDetailService(private val ktor: HttpClient) {
    suspend fun getStoryDetail(id: Long): StoryModel = ktor.get(
        urlString = "v0/item/$id.json",
        block = {},
    ).body()
}
