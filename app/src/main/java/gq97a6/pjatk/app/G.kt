package gq97a6.pjatk.app

import gq97a6.pjatk.app.Storage.parseListSave
import gq97a6.pjatk.app.Storage.parseSave

object G {
    var areInitialized = false

    var settings = Settings()
    var timetable = listOf<Course>()

    fun initialize() {
        settings = parseSave() ?: Settings()
        timetable = parseListSave()
    }
}