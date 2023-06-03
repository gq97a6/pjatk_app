package gq97a6.pjatk.app.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.*


@Composable
fun EditText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small
) {
    OutlinedTextField(
        enabled = enabled,
        readOnly = readOnly,
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(60.dp)
            .fillMaxWidth(),
        singleLine = singleLine,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = maxLines ?: 1,
        interactionSource = interactionSource,
        shape = shape,
        colors = editTextColors()
    )
}

@Composable
fun BasicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke = BorderStroke(2.dp, Colors.primary),
    contentPadding: PaddingValues = PaddingValues(13.dp),
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clickable(enabled, role = Role.Button, onClick = onClick)
            .border(border, shape)
            .padding(contentPadding),
        contentAlignment = contentAlignment
    ) {
        content()
    }
}

inline fun Modifier.nrClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

@Composable
fun LabeledCheckbox(
    label: @Composable (() -> Unit),
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Row(
        modifier = modifier.nrClickable(onClick = { onCheckedChange(!checked) }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp),
            checked = checked,
            enabled = enabled,
            interactionSource = interactionSource,
            colors = checkBoxColors(),
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.padding(5.dp))
        label()
    }
}

@Composable
fun BoldStartText(a: String, b: String, fontSize: TextUnit = 15.sp, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(text = a, fontWeight = FontWeight.Bold, fontSize = fontSize, color = Colors.primary)
        Text(text = b, fontSize = fontSize, color = Colors.primary)
    }
}

@Composable
inline fun FrameBox(
    a: String = "",
    b: String = "",
    modifier: Modifier = Modifier.fillMaxSize(),
    crossinline content: @Composable () -> Unit
) {
    Column {
        BoldStartText(
            a = a,
            b = b,
            modifier = Modifier.padding(start = 5.dp, bottom = 3.dp, top = 15.dp)
        )

        Surface(
            modifier = modifier
                .border(BorderStroke(1.dp, Colors.primary), RoundedCornerShape(10.dp))
                .padding(12.dp)
        ) {
            content()
        }
    }
}


@Composable
fun LabeledSwitch(
    label: @Composable (() -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Row(
        modifier = modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        label?.invoke()
        Text("OFF", fontSize = 12.sp, color = Colors.primary, modifier = Modifier.padding(start = 8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = switchColors(),
            interactionSource = interactionSource,
            modifier = Modifier
                .wrapContentSize()
                .padding(0.dp)
        )
        Text("ON", fontSize = 12.sp, color = Colors.primary)
    }
}

class NavShape(
    private val widthOffset: Dp,
    private val scale: Float
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                Offset.Zero,
                Offset(
                    size.width * scale + with(density) { widthOffset.toPx() },
                    size.height
                )
            )
        )
    }
}