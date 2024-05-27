package alessandro.vendramini.hackernews.presentation.components

import alessandro.vendramini.hackernews.presentation.ui.theme.HackerNewsTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HackerNewsTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    contentColor: Color = MaterialTheme.colorScheme.background,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Text(
                text = title,
                color = titleColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = contentColor),
        navigationIcon = {
            if (navigationIcon != null) {
                navigationIcon()
            }
        },
        actions = actions,
    )
}

@Composable
@Preview(showBackground = false)
private fun LightPreview() {
    HackerNewsTheme(darkTheme = false) {
        Column {
            HackerNewsTopAppBar(
                title = "Title",
            )
        }
    }
}
