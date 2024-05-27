package alessandro.vendramini.hackernews.presentation.components

import alessandro.vendramini.hackernews.data.models.StoryModel
import alessandro.vendramini.hackernews.presentation.ui.theme.HackerNewsTheme
import alessandro.vendramini.hackernews.util.convertUnixToLocalDate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StoryCard(
    storyModel: StoryModel,
    onCardClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            if (storyModel.url != null) {
                onCardClick()
            }
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = storyModel.title ?: "No title",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${storyModel.score} points by ${storyModel.by}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = AnnotatedString(
                        text = "${storyModel.descendants} comments",
                        spanStyle = SpanStyle(
                            textDecoration = TextDecoration.Underline,
                        ),
                    ),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = convertUnixToLocalDate(unixTimestamp = storyModel.time),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Preview
@Composable
private fun StoryCardPreview() {
    Column {
        HackerNewsTheme(darkTheme = false) {
            StoryCard(
                storyModel = StoryModel(
                    title = "Hello",
                    id = 0,
                ),
                onCardClick = {},
            )
        }
    }
}
