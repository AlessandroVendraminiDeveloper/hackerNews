package alessandro.vendramini.hackernews.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemModel(
    @SerialName("by") val by: String? = null,
    @SerialName("descendants") val descendants: Int = 0,
    @SerialName("id") val id: Long,
    @SerialName("kids") val kids: List<Long> = listOf(),
    @SerialName("parent") val parent: Long = 0,
    @SerialName("score") val score: Int = 0,
    @SerialName("time") val time: Long = 0,
    @SerialName("text") val text: String = "",
    @SerialName("title") val title: String? = null,
    @SerialName("type") val type: String = "story",
    @SerialName("url") val url: String?  = null,
) {
    fun convertToStoryModel(): StoryModel {
        return StoryModel(
            by = this.by,
            descendants = this.descendants,
            id = this.id,
            kids = this.kids,
            score = this.score,
            time = this.time,
            title = this.title,
            type = this.type,
            url = this.url,
        )
    }

    fun convertToCommentModel(): CommentModel {
        return CommentModel(
            by = this.by,
            id = this.id,
            kids = this.kids,
            parent = this.parent,
            time = this.time,
            text = this.text,
            type = this.type,
            url = this.url,
        )
    }
}
