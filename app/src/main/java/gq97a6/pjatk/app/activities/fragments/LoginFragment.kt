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
import androidx.fragment.app.Fragment
import gq97a6.pjatk.app.R
import gq97a6.pjatk.app.compose.BasicButton
import gq97a6.pjatk.app.compose.Colors
import gq97a6.pjatk.app.compose.EditText
import gq97a6.pjatk.app.compose.composeConstruct
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeConstruct(requireContext()) {

        var pass by remember { mutableStateOf("") }
        var login by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var rotationInitialValue by remember { mutableStateOf(0f) }
            var rotationTargetValue by remember { mutableStateOf(0f) }
            var scaleInitialValue by remember { mutableStateOf(1f) }
            var scaleTargetValue by remember { mutableStateOf(1f) }

            val rotation = rememberInfiniteTransition().animateFloat(
                initialValue = rotationInitialValue,
                targetValue = rotationTargetValue,
                animationSpec = infiniteRepeatable(
                    animation = tween(2500),
                    repeatMode = RepeatMode.Reverse,
                )
            )

            val alpha: Float by animateFloatAsState(
                targetValue = if (enabled) 1f else 0.5f,
                // Configure the animation duration and easing.
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )

            val scale = rememberInfiniteTransition().animateFloat(
                initialValue = scaleInitialValue,
                targetValue = scaleTargetValue,
                animationSpec = infiniteRepeatable(
                    animation = tween(2500),
                    repeatMode = RepeatMode.,
                )
            )

            Image(
                painterResource(R.drawable.logo_outline), "",
                modifier = Modifier
                    .padding(bottom = 60.dp, top = 180.dp)
                    .size(200.dp)
                    .scale(scale.value)
                    .rotate(rotation.value),
                colorFilter = ColorFilter.tint(Colors.primary)
            )

            var visible by remember { mutableStateOf(true) }
            AnimatedVisibility(
                visible = visible,
                //exit = slideOutVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(140.dp),
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
                        onClick = {
                            scaleTargetValue = .9f
                            rotationTargetValue = 360f

                            visible = false
                            GlobalScope.launch {
                                delay(2000)
                                visible = true

                                scaleInitialValue = scale.value
                                scaleTargetValue = 1f
                                rotationInitialValue = rotation.value
                                rotationTargetValue = 0f
                            }
                        },
                        Modifier
                            .fillMaxSize()
                            .padding(top = 6.dp)
                            .padding(start = 15.dp)
                    ) {
                    }
                }
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}