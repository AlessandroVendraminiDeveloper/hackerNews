package alessandro.vendramini.hackernews.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryModel(
    @SerialName("by") val by: String? = null,
    @SerialName("descendants") val descendants: Int = 0,
    @SerialName("id") val id: Long,
    @SerialName("kids") val kids: List<Long> = listOf(),
    @SerialName("score") val score: Int = 0,
    @SerialName("time") val time: Long = 0,
    @SerialName("title") val title: String? = null,
    @SerialName("type") val type: String = "story",
    @SerialName("url") val url: String?  = null,
)
