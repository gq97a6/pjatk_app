package gq97a6.pjatk.app.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

private val Colors = darkColors(
    primary = Purple80, secondary = PurpleGrey80
)

val Typography = Typography()

@Composable
fun PJATKTheme(
    darkTheme: Boolean = false, dynamicColor: Boolean = false, content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = Colors, typography = Typography, content = content
    )
}