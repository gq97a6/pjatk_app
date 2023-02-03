package gq97a6.pjatk.app.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.Fragment
import gq97a6.pjatk.app.compose.BasicButton
import gq97a6.pjatk.app.compose.EditText
import gq97a6.pjatk.app.Fetcher
import gq97a6.pjatk.app.compose.ComposeTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TimetableFragment : Fragment() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ComposeTheme {
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
                                    val courses = Fetcher.fetch("x", "x", 2)
                                    html = ""
                                    courses.forEach { html += it.toString() + "\n\n" }
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