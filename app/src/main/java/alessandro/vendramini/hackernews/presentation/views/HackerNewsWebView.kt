package alessandro.vendramini.hackernews.presentation.views

import alessandro.vendramini.hackernews.presentation.components.HackerNewsTopAppBar
import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HackerNewsWebView(
    navController: NavController,
    title: String?,
    url: String?,
) {
    /** States **/
    var showLoader by remember { mutableStateOf(true) }
    var webView: WebView? = null

    /** View ui **/
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HackerNewsTopAppBar(
                title = title ?: "-",
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        AndroidView(
            modifier = Modifier.padding(paddingValues),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )

                    settings.javaScriptEnabled = true
                    settings.databaseEnabled = true
                    settings.domStorageEnabled = true
                    settings.useWideViewPort = true
                    settings.allowFileAccess = true
                    settings.databaseEnabled = true
                    settings.safeBrowsingEnabled = true
                    settings.javaScriptCanOpenWindowsAutomatically = true
                    settings.loadWithOverviewMode = true
                    settings.loadsImagesAutomatically = true

                    webViewClient = object : WebViewClient() {
                        override fun onPageCommitVisible(view: WebView?, url: String?) {
                            super.onPageCommitVisible(view, url)
                            showLoader = false
                        }
                    }

                    if (url != null) {
                        loadUrl(url)
                    }
                    webView = this
                }
            },
            update = {
                webView = it
            },
        )
        if (showLoader) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(modifier = Modifier.size(32.dp))
            }
        }
    }

    /** Actions **/
    BackHandler(enabled = true) {
        if (webView?.canGoBack() == true) {
            webView!!.goBack()
        } else {
            navController.popBackStack()
        }
    }
}

@Composable
@Preview
private fun Preview() {
    HackerNewsWebView(
        navController = rememberNavController(),
        title = "WebView",
        url = "",
    )
}
