package alessandro.vendramini.hackernews.data.models

data class StoryModel(
    val by: String?,
    val descendants: Int,
    val id: Long,
    val kids: List<Long>,
    val score: Int,
    val time: Long,
    val title: String?,
    val type: String,
    val url: String?,
)
