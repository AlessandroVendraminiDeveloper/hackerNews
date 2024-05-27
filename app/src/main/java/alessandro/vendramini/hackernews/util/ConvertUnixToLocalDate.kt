package alessandro.vendramini.hackernews.util

import java.time.Duration
import java.time.Instant
import java.time.ZoneId

fun convertUnixToLocalDate(
    unixTimestamp: Long,
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    val instant = Instant.ofEpochSecond(unixTimestamp)
    val now = Instant.now()
    val duration = Duration.between(instant, now)

    return when {
        duration.toMinutes() < 1 -> "less than a minute ago"
        duration.toMinutes() == 1L -> "1 minute ago"
        duration.toMinutes() < 60 -> "${duration.toMinutes()} minutes ago"
        duration.toHours() == 1L -> "1 hour ago"
        duration.toHours() < 24 -> "${duration.toHours()} hours ago"
        duration.toDays() == 1L -> "1 day ago"
        duration.toDays() < 30 -> "${duration.toDays()} days ago"
        duration.toDays() < 365 -> "${duration.toDays() / 30} months ago"
        else -> "${duration.toDays() / 365} years ago"
    }
}