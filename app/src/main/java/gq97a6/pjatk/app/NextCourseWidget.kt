package gq97a6.pjatk.app

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import kotlin.random.Random

class NextCourseWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NextCourseWidget()
}

class NextCourseWidget : GlanceAppWidget() {

    @Composable
    override fun Content() {
        Column(
            verticalAlignment = Alignment.Vertical.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            G.timetable.courses.subList(0, 3).forEach {
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
                            text = "${Random.nextInt(0, 100)}m", modifier = GlanceModifier.width(60.dp),
                            style = TextStyle(color = ColorProvider(Color.White), textAlign = TextAlign.Center)
                        )
                        Text(
                            text = "${Random.nextInt(0, 100)}m", modifier = GlanceModifier.width(60.dp),
                            style = TextStyle(color = ColorProvider(Color.White), textAlign = TextAlign.End)
                        )
                    }
                }
            }

            Spacer(modifier = GlanceModifier.height(10.dp))

            Text(
                text = "REFRESH",
                modifier = GlanceModifier
                    .padding(top = 7.dp, bottom = 7.dp)
                    .fillMaxWidth()
                    .background(Color(41, 41, 41))
                    .clickable(actionRunCallback<AddWaterClickAction>()),
                style = TextStyle(color = ColorProvider(Color.White), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            )
        }
    }
}

class AddWaterClickAction : ActionCallback {
    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        //Fetcher.fetch(TMP.login, TMP.pass)
        NextCourseWidget().updateAll(context)
    }
}
