package gq97a6.pjatk.app.compose

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



val Typography = Typography()

@Composable
fun ComposeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = Colors,
        typography = Typography,
        shapes = Shapes(
            small = RoundedCornerShape(4.dp),
            medium = RoundedCornerShape(4.dp),
            large = RoundedCornerShape(4.dp)
        )
    ) {
        CompositionLocalProvider(
            LocalRippleTheme provides object : RippleTheme {
                @Composable
                override fun defaultColor(): Color = Colors.background

                @Composable
                override fun rippleAlpha(): RippleAlpha = RippleAlpha(.2f, .2f, .1f, 1f)
                //RippleTheme.defaultRippleAlpha(androidx.compose.ui.graphics.Color.Black, lightTheme = !isSystemInDarkTheme())
            },
            content = content
        )
    }
}