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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.Fragment
import androidx.glance.appwidget.updateAll
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TimetableFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = composeConstruct(requireContext()) {
        val scaffoldState = rememberScaffoldState()
        var isLoading by remember { mutableStateOf(false) }
        var courses by remember { mutableStateOf(timetable.day().groupBy { it.date }) }

        Scaffold(
            modifier = Modifier.padding(10.dp),
            scaffoldState = scaffoldState,
            content = { Content(courses) },
            topBar = { TopBar() },
            bottomBar = {
                BottomBar(
                    { isLoading = !isLoading },
                    { courses = timetable.day().groupBy { it.date } },
                    { courses = timetable.courses.groupBy { it.date } },
                    requireContext()
                )
            },
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
private fun Content(courses: Map<LocalDate, List<Course>>) =
    Box(contentAlignment = Alignment.Center) {
        var course by remember { mutableStateOf(null as Course?) }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            courses.forEach { (date, day) ->
                stickyHeader {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Colors.background)
                    ) {
                        Text(
                            text = "${date.dzien()} ${date.format(DateTimeFormatter.ofPattern("dd.MM"))}",
                            fontSize = 30.sp,
                            color = Colors.secondary
                        )
                    }
                }

                items(day) { c ->
                    Column(modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                        .clickable {
                            course = c
                        }) {
                        Text(
                            text = "${c.name} ${if (c.type == "Wykład") "(W)" else ""}",
                            fontSize = 20.sp,
                            color = Colors.primary
                        )
                        Text(text = "Od: ${c.startString}", color = Colors.primary)
                        Text(text = "Do: ${c.endString}", color = Colors.primary)
                        Text(text = "Sala: ${c.room}", color = Colors.primary)
                    }
                }

                item { Spacer(modifier = Modifier.height(30.dp)) }
            }
        }

        if (course != null) {
            val c = course ?: Course()

            Dialog({ course = null }) {
                Column(
                    modifier = Modifier
                        .background(Colors.background.copy(0.6f))
                        .border(BorderStroke(2.dp, Colors.primary), MaterialTheme.shapes.small)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = c.name, fontSize = 25.sp, color = Colors.secondary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Kod: ${c.code}", color = Colors.primary)
                    Text(text = "Typ: ${c.type}", color = Colors.primary)
                    Text(text = "Grupy: ${c.groups}", color = Colors.primary)
                    Text(text = "Budynek: ${c.building}", color = Colors.primary)
                    Text(text = "Sala: ${c.room}", color = Colors.primary)
                    Text(text = "Od: ${c.start}", color = Colors.primary)
                    Text(text = "Do: ${c.end}", color = Colors.primary)
                    Text(
                        text = "Data: ${c.date.format(DateTimeFormatter.ofPattern("dd.MM"))}",
                        color = Colors.primary
                    )
                    Text(
                        text = "Prowadzący: ${c.educators.joinToString(", ")}",
                        color = Colors.primary
                    )
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
    horizontalArrangement = Arrangement.SpaceBetween,
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

    BasicButton(
        onClick = {

        },
        contentPadding = PaddingValues(1.dp),
        border = BorderStroke(1.dp, Colors.primary),
        modifier = Modifier
            .height(30.dp)
            .width(80.dp)
    ) {
        Text("SETTINGS", fontSize = 10.sp, color = Colors.primary)
    }
}

@Composable
private fun BottomBar(
    getOnClick: () -> Unit,
    dayOnClick: () -> Unit,
    allOnClick: () -> Unit,
    context: Context
) = Row(
    horizontalArrangement = Arrangement.SpaceAround,
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
        .height(70.dp)
        .fillMaxWidth()
        .background(Colors.background)
) {
    BasicButton(
        onClick = {
            getOnClick()

            CoroutineScope(Dispatchers.IO).launch {
                Fetcher.fetch {
                    if (it.first != null) {
                        G.timetable.update(it.first ?: listOf())
                        NextCourseWidget().updateAll(context)
                    } else createToast(context, it.second)
                }

                getOnClick()
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
        onClick = dayOnClick,
        contentPadding = PaddingValues(10.dp),
        border = BorderStroke(1.dp, Colors.primary),
        modifier = Modifier
            .height(50.dp)
    ) {
        Text("DAY", fontSize = 15.sp, color = Colors.primary)
    }

    BasicButton(
        onClick = allOnClick,
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