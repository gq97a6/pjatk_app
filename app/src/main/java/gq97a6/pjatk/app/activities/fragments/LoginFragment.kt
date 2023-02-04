package gq97a6.pjatk.app.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import gq97a6.pjatk.app.Fetcher
import gq97a6.pjatk.app.G
import gq97a6.pjatk.app.G.settings
import gq97a6.pjatk.app.G.timetable
import gq97a6.pjatk.app.R
import gq97a6.pjatk.app.Storage.saveToFile
import gq97a6.pjatk.app.activities.MainActivity.Companion.fm
import gq97a6.pjatk.app.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeConstruct(requireContext()) {

        var login by remember { mutableStateOf("") }
        var pass by remember { mutableStateOf("") }
        var text by remember { mutableStateOf("Error") }
        var save by remember { mutableStateOf(false) }

        var buttonEnabled by remember { mutableStateOf(true) }
        var uiVisible by remember { mutableStateOf(true) }
        var textVisible by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val rotation = animateFloatAsState(
                targetValue = if (uiVisible) 0f else 360f,
                animationSpec = if (uiVisible) tween(durationMillis = 800)
                else infiniteRepeatable(
                    animation = tween(2000),
                    repeatMode = RepeatMode.Reverse,
                )
            )
            val scale = animateFloatAsState(
                targetValue = if (uiVisible) 1f else .9f,
                animationSpec = if (uiVisible) tween(durationMillis = 800)
                else infiniteRepeatable(
                    animation = tween(2000),
                    repeatMode = RepeatMode.Reverse,
                )
            )

            Image(
                painterResource(R.drawable.logo_outline), "",
                modifier = Modifier
                    .padding(top = 160.dp)
                    .size(200.dp)
                    .scale(scale.value)
                    .rotate(rotation.value),
                colorFilter = ColorFilter.tint(Colors.primary)
            )

            Column(
                modifier = Modifier.height(60.dp),
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(
                    visible = textVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(text, fontSize = 18.sp, color = Colors.primary)
                }
            }

            AnimatedVisibility(
                visible = uiVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.height(140.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth(.75f)
                                .fillMaxHeight()
                        ) {
                            EditText(
                                label = { Text("Login") },
                                modifier = Modifier,
                                value = login,
                                onValueChange = {
                                    login = it
                                })

                            EditText(
                                label = { Text("Password") },
                                modifier = Modifier,
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                value = pass,
                                onValueChange = {
                                    pass = it
                                })
                        }

                        BasicButton(
                            enabled = buttonEnabled,
                            onClick = {
                                buttonEnabled = false
                                uiVisible = false
                                textVisible = false

                                CoroutineScope(Dispatchers.IO).launch {
                                    var success = false
                                    try {
                                        Fetcher.fetch(login, pass).let {
                                            if (it == null) return@let "Wystąpił błąd"
                                            if (save) {
                                                settings.login = login
                                                settings.pass = pass
                                                settings.saveToFile()
                                            }

                                            timetable = it
                                            timetable.saveToFile()

                                            success = true
                                            fm.replaceWith(TimetableFragment(), false)
                                        }
                                    } catch (e: Fetcher.FetchException) {
                                        text = e.message
                                    } catch (e: Exception) {
                                        text = "Wystąpił błąd"
                                    }

                                    if (!success) {
                                        buttonEnabled = true
                                        uiVisible = true
                                        textVisible = true
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 6.dp)
                                .padding(start = 15.dp)
                        ) {
                        }
                    }

                    LabeledCheckbox(
                        modifier = Modifier.padding(top = 20.dp),
                        checked = save,
                        onCheckedChange = { save = it },
                        label = {
                            Text(
                                "Save credentials",
                                fontSize = 15.sp,
                                color = Colors.secondary
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}