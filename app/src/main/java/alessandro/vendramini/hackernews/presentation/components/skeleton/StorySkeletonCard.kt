package alessandro.vendramini.hackernews.presentation.components.skeleton

import alessandro.vendramini.hackernews.presentation.components.shimmer.shimmerEffect
import alessandro.vendramini.hackernews.presentation.ui.theme.HackerNewsTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StorySkeletonCard(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .shimmerEffect(),
    )
}

@Preview
@Composable
private fun ProjectSkeletonCardPreviewLight() {
    HackerNewsTheme(darkTheme = false) {
        StorySkeletonCard()
    }
}
