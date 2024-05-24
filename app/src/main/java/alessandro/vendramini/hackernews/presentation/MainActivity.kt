package alessandro.vendramini.hackernews.presentation

import alessandro.vendramini.hackernews.presentation.ui.theme.HackerNewsTheme
import alessandro.vendramini.hackernews.presentation.views.DashboardView
import alessandro.vendramini.hackernews.presentation.viewmodels.DashboardViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            HackerNewsTheme {
                val viewModel = koinViewModel<DashboardViewModel>()
                val uiState by viewModel.uiState.collectAsState()

                viewModel.aaa()
                DashboardView(
                    navController = rememberNavController(),
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                )
            }
        }
    }
}