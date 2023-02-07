package gq97a6.pjatk.app

import gq97a6.pjatk.app.Storage.saveToFile
import java.time.LocalDate
import java.util.*

class Timetable(var courses: List<Course> = listOf()) {

    var fetched = Date(0)

    fun update(value: List<Course>) {
        courses = value.sortedWith(compareBy(Course::date, Course::start, Course::end))
        fetched = Date()
        this.saveToFile()
    }

    fun day(date: LocalDate = LocalDate.now()): List<Course> =
        courses.filter { (it.date).compareTo(date) == 0 }

    fun week(date: LocalDate = LocalDate.now()): Map<LocalDate, List<Course>> {
        val start = date.minusDays(date.dayOfWeek.value.toLong())
        val end = start.plusWeeks(1)
        return courses
            .filter { it.date.isAfter(start) && it.date.isBefore(end) }
            .groupBy { it.date }
    }
}