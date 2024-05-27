package alessandro.vendramini.hackernews.data.models

data class CommentModel(
    val by: String?,
    val id: Long,
    val kids: List<Long>,
    val parent: Long,
    val text: String,
    val time: Long,
    val type: String,
    val url: String?,
)
