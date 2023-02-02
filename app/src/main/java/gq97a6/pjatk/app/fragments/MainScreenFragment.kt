package gq97a6.pjatk.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.Fragment
import gq97a6.pjatk.app.Fetcher
import gq97a6.pjatk.app.theme.PJATKTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainScreenFragment : Fragment() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PJATKTheme {
                    var html by remember { mutableStateOf("ŁADOWANIE...") }

                    var pass by remember { mutableStateOf("") }
                    var login by remember { mutableStateOf("") }

                    var hasLogin by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        EditText(
                            label = { Text("Login") },
                            modifier = Modifier.padding(20.dp),
                            value = login,
                            onValueChange = {
                                login = it
                            })

                        EditText(
                            label = { Text("Password") },
                            modifier = Modifier.padding(20.dp),
                            visualTransformation = PasswordVisualTransformation(),
                            value = pass,
                            onValueChange = {
                                pass = it
                            })

                        BasicButton(
                            onClick = {
                                hasLogin = true
                                //GlobalScope.launch { html = Fetcher.fetch(login, pass) }
                                GlobalScope.launch {
                                    html = ""
                                    Fetcher.fetch("X", "X").forEach {
                                        html += it.toString() + "\n\n"
                                    }
                                }
                            },
                            Modifier
                                .padding(20.dp)
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            Text(text = "Login")
                        }

                        if (hasLogin) {
                            Dialog({ hasLogin = false }) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 50.dp, horizontal = 10.dp)
                                        .background(Color.White)
                                ) {
                                    Text(text = html, modifier = Modifier.padding(10.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

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
        shape = shape
    )
}

@Composable
fun BasicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke = BorderStroke(2.dp, Color.Black),
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