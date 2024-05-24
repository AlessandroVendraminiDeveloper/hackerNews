package alessandro.vendramini.hackernews.data.api.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class NewStoriesService(private val ktor: HttpClient) {
    suspend fun getNewStoriesIds(): List<Long> = ktor.get(
        urlString = "v0/topstories.json",
        block = {},
    ).body()
}
