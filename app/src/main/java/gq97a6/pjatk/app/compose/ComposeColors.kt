package gq97a6.pjatk.app.compose

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val pallet = ComposeColorPallet(
    Color(237, 28, 36),
    Color(0, 0, 0),
    Color(255, 255, 255),
    Color(197, 25, 32),
    Color(177, 22, 28),
    Color(133, 11, 16),
    Color(97, 18, 21)
)

val Colors = lightColors(
    primary = Color(237, 28, 36),
    primaryVariant = Color(237, 28, 36),
    secondary = Color(255, 255, 255),
    secondaryVariant = Color(237, 28, 36),
    background = Color(0, 0, 0, 255),
    surface = Color(255, 255, 255, 0),
    error = Color(237, 28, 36),
    onPrimary = Color(237, 28, 36),
    onSecondary = Color(237, 28, 36),
    onBackground = Color(237, 28, 36),
    onSurface = Color(237, 28, 36),
    onError = Color(237, 28, 36)
)

@Composable
fun editTextColors() = TextFieldDefaults.outlinedTextFieldColors(
    textColor = pallet.b,
    cursorColor = pallet.b,
    focusedBorderColor = pallet.a,
    focusedLabelColor = pallet.a,
    unfocusedBorderColor = pallet.b,
    unfocusedLabelColor = pallet.b,
    disabledTextColor = pallet.b,
    disabledBorderColor = pallet.b,
    disabledLabelColor = pallet.b,
    leadingIconColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
    trailingIconColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
)

@Composable
fun checkBoxColors() = CheckboxDefaults.colors(
    checkedColor = pallet.b,
    uncheckedColor = pallet.a,
    checkmarkColor = pallet.background
)

@Composable
fun switchColors() = SwitchDefaults.colors(
    checkedThumbColor = pallet.a,
    checkedTrackColor = pallet.b,
    uncheckedThumbColor = pallet.b,
    uncheckedTrackColor = pallet.c,
)

class ComposeColorPallet(
    val color: Color,
    val background: Color,
    val accent: Color,
    val a: Color,
    val b: Color,
    val c: Color,
    val d: Color
)