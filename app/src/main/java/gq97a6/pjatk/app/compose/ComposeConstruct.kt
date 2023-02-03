package gq97a6.pjatk.app.compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView

fun composeConstruct(context: Context, content: @Composable () -> Unit) =
    ComposeView(context).apply { setContent { ComposeTheme(content) } }

