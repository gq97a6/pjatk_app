package gq97a6.pjatk.app.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Colors = darkColors(
    primary = Color(237, 28, 36), secondary = Color(255,255,255), background = Color(0,0,0)
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