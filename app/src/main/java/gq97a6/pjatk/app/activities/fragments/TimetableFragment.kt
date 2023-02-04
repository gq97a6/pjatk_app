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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.Fragment
import gq97a6.pjatk.app.Fetcher
import gq97a6.pjatk.app.G
import gq97a6.pjatk.app.G.settings
import gq97a6.pjatk.app.Storage.saveToFile
import gq97a6.pjatk.app.activities.MainActivity
import gq97a6.pjatk.app.compose.*
import kotlinx.coroutines.*

class TimetableFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeConstruct(requireContext()) {
        BasicButton(
            onClick = {
                settings.login = ""
                settings.pass = ""
                settings.saveToFile()

                MainActivity.fm.replaceWith(LoginFragment(), false)
            },
            modifier = Modifier.size(100.dp)
        ) {
            Text("LOGOUT", fontSize = 18.sp, color = Colors.primary)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}