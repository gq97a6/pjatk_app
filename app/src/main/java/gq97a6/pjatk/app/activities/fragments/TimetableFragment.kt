package gq97a6.pjatk.app.activities.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import gq97a6.pjatk.app.*
import gq97a6.pjatk.app.G.settings
import gq97a6.pjatk.app.G.timetable
import gq97a6.pjatk.app.R
import gq97a6.pjatk.app.Storage.saveToFile
import gq97a6.pjatk.app.activities.MainActivity
import gq97a6.pjatk.app.compose.BasicButton
import gq97a6.pjatk.app.compose.Colors
import gq97a6.pjatk.app.compose.NavShape
import gq97a6.pjatk.app.compose.composeConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
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
        var isLoading by remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.padding(10.dp),
            scaffoldState = scaffoldState,
            content = { Content() },
            topBar = { TopBar() },
            bottomBar = { BottomBar({ isLoading = !isLoading }, requireContext()) },
            drawerContent = { Drawer() },
            drawerBackgroundColor = Color(0, 0, 0, 0),
            drawerContentColor = Color(0, 0, 0, 0),
            drawerScrimColor = Color(0, 0, 0, 220),
            backgroundColor = Color(0, 0, 0, 255),
            drawerShape = NavShape(0.dp, 0.6f)
        )

        LoadingScreen(isLoading)
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
                Row(
                    modifier = Modifier
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
private fun TopBar() = Row(
    horizontalArrangement = Arrangement.Start,
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
        .height(50.dp)
        .fillMaxWidth()
        .background(Colors.background)
) {
    BasicButton(
        onClick = {
            settings = Settings()
            timetable = Timetable()

            settings.saveToFile()
            timetable.saveToFile()

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
private fun BottomBar(toggleLoading: () -> Unit, context: Context) = Row(
    horizontalArrangement = Arrangement.SpaceAround,
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
        .height(70.dp)
        .fillMaxWidth()
        .background(Colors.background)
) {
    BasicButton(
        onClick = {
            toggleLoading()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Fetcher.fetch(settings.login, settings.pass, settings.weeks).let { success ->
                        if (!success) createToast(context, "Failed")
                    }
                } catch (e: Fetcher.FetchException) {
                    createToast(context, e.message)
                } catch (e: Exception) {
                    createToast(context, "Error")
                }

                toggleLoading()
            }
        },
        contentPadding = PaddingValues(10.dp),
        border = BorderStroke(1.dp, Colors.primary),
        modifier = Modifier
            .height(50.dp)
    ) {
        Text("GET", fontSize = 15.sp, color = Colors.primary)
    }

    BasicButton(
        onClick = {
        },
        contentPadding = PaddingValues(10.dp),
        border = BorderStroke(1.dp, Colors.primary),
        modifier = Modifier
            .height(50.dp)
    ) {
        Text("DAY", fontSize = 15.sp, color = Colors.primary)
    }

    BasicButton(
        onClick = {
        },
        contentPadding = PaddingValues(10.dp),
        border = BorderStroke(1.dp, Colors.primary),
        modifier = Modifier
            .height(50.dp)
    ) {
        Text("ALL", fontSize = 15.sp, color = Colors.primary)
    }
}

@Composable
private fun LoadingScreen(isLoading: Boolean) {
    val rotation = animateFloatAsState(
        targetValue = if (isLoading) 360f else 0f,
        animationSpec = if (!isLoading) tween(durationMillis = 800)
        else infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse,
        )
    )
    val scale = animateFloatAsState(
        targetValue = if (isLoading) .9f else 1f,
        animationSpec = if (!isLoading) tween(durationMillis = 800)
        else infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse,
        )
    )

    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.background)
        ) {
            Image(
                painterResource(R.drawable.logo_outline), "",
                modifier = Modifier
                    .padding(top = 160.dp)
                    .size(200.dp)
                    .scale(scale.value)
                    .rotate(rotation.value),
                colorFilter = ColorFilter.tint(Colors.primary)
            )
        }
    }
}