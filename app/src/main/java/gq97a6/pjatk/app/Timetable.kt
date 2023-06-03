package gq97a6.pjatk.app

import gq97a6.pjatk.app.objects.Storage.saveToFile
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class Timetable(var courses: List<Course> = listOf()) {

    var fetched = Date(0)

    fun update(value: List<Course>) {
        courses = value.sortedWith(compareBy(Course::date, Course::start, Course::end))
        fetched = Date()
        this.saveToFile()
    }

    //Get courses after specific time in current day
    fun later(dateTime: LocalDateTime = LocalDateTime.now()): List<Course> =
        courses.filter {
            it.date.atTime(it.end).isAfter(dateTime) &&
                    it.date.compareTo(dateTime.toLocalDate()) == 0
        }

    //Get courses after specific day
    fun after(dateTime: LocalDateTime = LocalDate.now().atStartOfDay()): List<Course> =
        courses.filter { it.date.atTime(it.end).isAfter(dateTime) }

    //Get courses for the day
    fun day(date: LocalDate = LocalDate.now()): List<Course> =
        courses.filter { it.date.compareTo(date) == 0 }

    //Get courses for the week
    fun week(date: LocalDate = LocalDate.now()): Map<LocalDate, List<Course>> {
        val start = date.minusDays(date.dayOfWeek.value.toLong())
        val end = start.plusWeeks(1)
        return courses
            .filter { it.date.isAfter(start) && it.date.isBefore(end) }
            .groupBy { it.date }
    }
}