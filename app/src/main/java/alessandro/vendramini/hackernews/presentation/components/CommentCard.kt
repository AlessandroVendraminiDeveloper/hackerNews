package alessandro.vendramini.hackernews.presentation.components

import alessandro.vendramini.hackernews.data.models.CommentModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat

@Composable
fun CommentCard(
    commentModel: CommentModel,
    onShowMoreClick: (Pair<Long, List<Long>>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {}
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "By: ${commentModel.by ?: "Anonymous"}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = buildAnnotatedString {
                    append(HtmlCompat.fromHtml(commentModel.text, HtmlCompat.FROM_HTML_MODE_COMPACT))
                },
                fontSize = 12.sp,
            )
            if (commentModel.answers.isNotEmpty()) {
                commentModel.answers.forEach { answer ->
                    CommentCard(
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                        commentModel = answer,
                        onShowMoreClick = { pair ->
                            onShowMoreClick(
                                Pair(
                                    pair.first,
                                    pair.second,
                                )
                            )
                        }
                    )
                }
            }
            if (commentModel.kids.isNotEmpty() && commentModel.answers.isEmpty()) {
                Text(
                    modifier = Modifier
                        .clickable {
                            onShowMoreClick(
                                Pair(
                                    commentModel.id,
                                    commentModel.kids,
                                )
                            )
                        },
                    text = AnnotatedString(
                        text = "Show answers ${commentModel.kids.size}",
                        spanStyle = SpanStyle(
                            textDecoration = TextDecoration.Underline,
                        ),
                    ),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
