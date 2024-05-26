package alessandro.vendramini.hackernews.util

data class PaginationState(
    val isLoading: Boolean = false,
    val page: Int = 0,
    val endReached: Boolean = false,
)