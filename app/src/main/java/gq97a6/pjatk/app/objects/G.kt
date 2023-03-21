package gq97a6.pjatk.app.objects

import gq97a6.pjatk.app.Settings
import gq97a6.pjatk.app.objects.Storage.parseSave
import gq97a6.pjatk.app.Timetable

object G {
    var areInitialized = false

    var settings = Settings()
    var timetable = Timetable()

    fun initialize() {
        timetable = parseSave() ?: Timetable()
        settings = parseSave() ?: Settings()
    }
}