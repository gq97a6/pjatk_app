package gq97a6.pjatk.app.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import gq97a6.pjatk.app.createToast
import gq97a6.pjatk.app.objects.Fetcher
import gq97a6.pjatk.app.objects.G
import gq97a6.pjatk.app.objects.G.settings
import gq97a6.pjatk.app.objects.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NextCourseWidget : GlanceAppWidget() {

    companion object {
        var updateEnabled = true
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Column(
                verticalAlignment = Alignment.Vertical.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                if (updateEnabled) {
                    G.timetable.later().take(3).forEach {
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
                        modifier = GlanceModifier
                            .background(Color(41, 41, 41))
                            .height(35.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "UPDATE",
                            modifier = GlanceModifier
                                .width(200.dp)
                                .fillMaxHeight()
                                .padding(start = 50.dp, top = 7.dp)
                                .clickable(actionRunCallback<UpdateAction>()),
                            style = TextStyle(
                                color = ColorProvider(Color.White),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        )

                        Text(
                            text = "REFRESH",
                            modifier = GlanceModifier
                                .width(200.dp)
                                .fillMaxHeight()
                                .padding(end = 50.dp, top = 7.dp)
                                .clickable(actionRunCallback<RefreshAction>()),
                            style = TextStyle(
                                color = ColorProvider(Color.White),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = GlanceModifier.fillMaxSize()
                    ) {
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
        override suspend fun onAction(
            context: Context,
            glanceId: GlanceId,
            parameters: ActionParameters
        ) {
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
        override suspend fun onAction(
            context: Context,
            glanceId: GlanceId,
            parameters: ActionParameters
        ) {
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
