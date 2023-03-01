package gq97a6.pjatk.app

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import gq97a6.pjatk.app.G.settings
import gq97a6.pjatk.app.NextCourseWidget.Companion.updateEnabled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

class NextCourseWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NextCourseWidget()
}

class NextCourseWidget : GlanceAppWidget() {

    companion object {
        var updateEnabled = true
    }

    @Composable
    override fun Content() {
        Column(
            verticalAlignment = Alignment.Vertical.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            if (updateEnabled) {
                G.timetable.later(LocalDateTime.now()).take(3).forEach {
                    it.apply {
                        Row {
                            Text(
                                text = code,
                                modifier = GlanceModifier.width(60.dp),
                                style = TextStyle(color = ColorProvider(Color.White))
                            )
                            Text(
                                text = room, modifier = GlanceModifier.width(100.dp),
                                style = TextStyle(color = ColorProvider(Color.White))
                            )
                            Text(
                                text = startIn,
                                modifier = GlanceModifier.width(60.dp),
                                style = TextStyle(
                                    color = ColorProvider(Color.White),
                                    textAlign = TextAlign.Center
                                )
                            )
                            Text(
                                text = endIn,
                                modifier = GlanceModifier.width(60.dp),
                                style = TextStyle(
                                    color = ColorProvider(Color.White),
                                    textAlign = TextAlign.End
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = GlanceModifier.height(10.dp))

                Row(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = GlanceModifier
                        .background(Color(41, 41, 41))
                        .height(35.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "UPDATE",
                        modifier = GlanceModifier
                            .width(200.dp)
                            .clickable(actionRunCallback<UpdateAction>()),
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )
                    )

                    Spacer(modifier = GlanceModifier.width(80.dp))

                    Text(
                        text = "REFRESH",
                        modifier = GlanceModifier
                            .width(200.dp)
                            .clickable(actionRunCallback<RefreshAction>()),
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    )
                }
            } else {
                Box(contentAlignment = Alignment.Center, modifier = GlanceModifier.fillMaxSize()) {
                    Text(
                        text = "Updating...",
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 30.sp
                        )
                    )
                }
            }
        }
    }
}

class RefreshAction : ActionCallback {
    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        context.apply {
            if (!G.areInitialized) {
                Storage.rootFolder = filesDir.canonicalPath.toString()
                G.initialize()
            }
        }
        NextCourseWidget().updateAll(context)
    }
}

class UpdateAction : ActionCallback {
    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        if (updateEnabled) {
            updateEnabled = false
            NextCourseWidget().updateAll(context)

            context.apply {
                if (!G.areInitialized) {
                    Storage.rootFolder = filesDir.canonicalPath.toString()
                    G.initialize()
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                Fetcher.fetch(settings.login, settings.pass) {
                    if (it.first == null) createToast(context, it.second)
                    else createToast(context, "Success")

                    updateEnabled = true
                    NextCourseWidget().updateAll(context)
                }
            }
        }
    }
}
