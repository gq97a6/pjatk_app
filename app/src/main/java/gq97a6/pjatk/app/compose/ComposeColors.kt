package gq97a6.pjatk.app.compose

import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Colors = lightColors(
    primary = Color(237, 28, 36),
    primaryVariant = Color(237, 28, 36),
    secondary = Color(255, 255, 255),
    secondaryVariant = Color(237, 28, 36),
    background = Color(0, 0, 0),
    surface = Color(237, 28, 36),
    error = Color(237, 28, 36),
    onPrimary = Color(237, 28, 36),
    onSecondary = Color(237, 28, 36),
    onBackground = Color(237, 28, 36),
    onSurface = Color(237, 28, 36),
    onError = Color(237, 28, 36)
)

@Composable
fun editTextColors() = TextFieldDefaults.outlinedTextFieldColors(
    textColor = Colors.secondary,
    cursorColor = Colors.secondary,
    focusedBorderColor = Colors.primary,
    focusedLabelColor = Colors.primary,
    unfocusedBorderColor = Colors.secondary,
    unfocusedLabelColor = Colors.secondary,
    disabledTextColor = Colors.secondary,
    disabledBorderColor = Colors.secondary,
    disabledLabelColor = Colors.secondary,
    leadingIconColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
    trailingIconColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
)

@Composable
fun CheckBoxColors() = CheckboxDefaults.colors(
    checkedColor = Colors.primary,
    uncheckedColor = Colors.secondary,
    checkmarkColor = Colors.secondary
)