package gq97a6.pjatk.app

import gq97a6.pjatk.app.Storage.saveToFile
import java.util.*

class Timetable(courses: List<Course> = listOf()) {

    var fetched = Date(0)
    var courses = listOf<Course>()
        set(value) {
            field = value
            fetched = Date()
            this.saveToFile()
        }

    init {
        this.courses = courses
    }
}