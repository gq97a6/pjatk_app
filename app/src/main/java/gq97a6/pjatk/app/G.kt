package gq97a6.pjatk.app

import gq97a6.pjatk.app.Storage.parseSave

object G {
    var areInitialized = false

    var settings = Settings()
    var timetable = Timetable()

    fun initialize() {
        timetable = parseSave() ?: Timetable()
        settings = parseSave() ?: Settings()
    }
}