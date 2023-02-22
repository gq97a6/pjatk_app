package gq97a6.pjatk.app.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import gq97a6.pjatk.app.G.settings
import gq97a6.pjatk.app.G.timetable
import gq97a6.pjatk.app.Storage.saveToFile
import gq97a6.pjatk.app.activities.MainActivity
import gq97a6.pjatk.app.compose.BasicButton
import gq97a6.pjatk.app.compose.Colors
import gq97a6.pjatk.app.compose.NavShape
import gq97a6.pjatk.app.compose.composeConstruct

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

class TimetableFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = composeConstruct(requireContext()) {
        val scaffoldState = rememberScaffoldState()

        Scaffold(
            modifier = Modifier.padding(10.dp),
            scaffoldState = scaffoldState,
            content = { Content() },
            topBar = { TopBar() },
            bottomBar = { BottomBar() },
            drawerContent = { Drawer() },
            drawerBackgroundColor = Color(0, 0, 0, 0),
            drawerContentColor = Color(0, 0, 0, 0),
            drawerScrimColor = Color(0, 0, 0, 220),
            backgroundColor = Color(0, 0, 0, 255),
            drawerShape = NavShape(0.dp, 0.6f)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content() = Column() {
    val tt = remember { timetable.courses.groupBy { it.date } }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        tt.forEach { date, day ->
            stickyHeader {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                ) {
                    Text(text = date.dayOfWeek.toString(), fontSize = 30.sp, color = Colors.primary)
                }
            }

            items(day) { c ->
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(text = "${c.code}", fontSize = 20.sp, color = Colors.primary)
                    Text(text = "Od: ${c.startString}", color = Colors.primary)
                    Text(text = "Do: ${c.endString}", color = Colors.primary)
                    Text(text = "Sala: ${c.room}", color = Colors.primary)
                }
            }
        }
    }
}

@Composable
private fun Drawer() =
    Column(
        modifier = Modifier
            .fillMaxWidth(.6f)
            .fillMaxHeight()
            .padding(vertical = 20.dp)
            .padding(start = 10.dp)
            .border(1.dp, Color.Red, RoundedCornerShape(5.dp))
    ) {

    }

@Composable
private fun TopBar() = Row {
    BasicButton(
        onClick = {
            settings.login = ""
            settings.pass = ""
            settings.saveToFile()

            MainActivity.fm.replaceWith(LoginFragment(), false)
        },
        contentPadding = PaddingValues(1.dp),
        border = BorderStroke(1.dp, Colors.primary),
        modifier = Modifier
            .height(30.dp)
            .width(80.dp)
    ) {
        Text("LOGOUT", fontSize = 10.sp, color = Colors.primary)
    }
}

@Composable
private fun BottomBar() = Row {
    BasicButton(
        onClick = {
        },
        contentPadding = PaddingValues(10.dp),
        border = BorderStroke(1.dp, Colors.primary),
        modifier = Modifier
            .height(50.dp)
    ) {
        Text("20", fontSize = 20.sp, color = Colors.primary)
    }
}