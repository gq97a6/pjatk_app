package gq97a6.pjatk.app.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import gq97a6.pjatk.app.G.settings
import gq97a6.pjatk.app.G.timetable
import gq97a6.pjatk.app.Storage.saveToFile
import gq97a6.pjatk.app.activities.MainActivity
import gq97a6.pjatk.app.compose.BasicButton
import gq97a6.pjatk.app.compose.Colors
import gq97a6.pjatk.app.compose.composeConstruct
import java.time.LocalDate

class TimetableFragment : Fragment() {

    val day = timetable.courses

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeConstruct(requireContext()) {
        Column {
            BasicButton(
                onClick = {
                    settings.login = ""
                    settings.pass = ""
                    settings.saveToFile()

                    MainActivity.fm.replaceWith(LoginFragment(), false)
                },
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            ) {
                Text("LOGOUT", fontSize = 18.sp, color = Colors.primary)
            }

            for (c in day) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(text = "${c.code}", fontSize = 20.sp, color = Colors.primary)
                    Text(text = "Od: ${c.startString}", color = Colors.primary)
                    Text(text = "Do: ${c.endString}", color = Colors.primary)
                    Text(text = "Sala: ${c.room}", color = Colors.primary)
                }
            }
        }

        /*
        przycisk wylogowania
        przycisk odświeżenia
        aktualność danych

        ustawienia
            wymagania aktualności danych
            najbliższy dzień czy dzisiaj jako day view
            default view

        widok tygodnia
            dodatkowe info

        widok dzisiaj
            dodatkowe info
        */
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}