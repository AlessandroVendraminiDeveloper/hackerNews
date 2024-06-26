package alessandro.vendramini.hackernews.presentation.components

import alessandro.vendramini.hackernews.presentation.navigations.NAVIGATION_BAR_DESTINATIONS
import alessandro.vendramini.hackernews.presentation.navigations.NavigationBarModel
import alessandro.vendramini.hackernews.presentation.ui.theme.HackerNewsTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HackerNewsNavigationBar(
    selectedDestination: String,
    isVisible: Boolean,
    navigationActions: (NavigationBarModel) -> Unit,
) {
    if (isVisible) {
        NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
            NAVIGATION_BAR_DESTINATIONS.forEach { navigationBarDestination ->
                NavigationBarItem(
                    label = {
                        Text(
                            text = stringResource(navigationBarDestination.titleId),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = navigationBarDestination.icon,
                            contentDescription = stringResource(
                                id = navigationBarDestination.titleId,
                            ),
                        )
                    },
                    selected = selectedDestination == navigationBarDestination.route,
                    onClick = { navigationActions(navigationBarDestination) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Column {
        HackerNewsTheme(darkTheme = false) {
            HackerNewsNavigationBar(
                selectedDestination = "",
                isVisible = true,
                navigationActions = {},
            )
        }
        HackerNewsTheme(darkTheme = true) {
            HackerNewsNavigationBar(
                selectedDestination = "",
                isVisible = true,
                navigationActions = {},
            )
        }
    }
}
