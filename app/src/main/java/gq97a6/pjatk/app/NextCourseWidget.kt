package gq97a6.pjatk.app

import androidx.compose.runtime.Composable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class NextCourseWidget : GlanceAppWidget() {
    @Composable
    override fun Content() {
    }
}

class NextCourseWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NextCourseWidget()
}