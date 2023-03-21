package gq97a6.pjatk.app

import gq97a6.pjatk.app.objects.Storage.saveToFile
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class Timetable(var courses: List<Course> = listOf()) {

    init {
        //val days = listOf("22.02.2023", "23.02.2023", "24.02.2023", "25.02.2023", "26.02.2023", "27.02.2023", "28.02.2023")
        //val empty = mutableListOf<Course>()
//
        //for(i in 0..6) {
        //    empty.addAll(listOf(
        //        Course("Metody programowania", "MPR", "Wykład", "", listOf("Czarnowski Mateusz", "Dubiela Piotr"), "", "A205-206", days[i], "12:00:00", "12:30:00", "30 min", ""),
        //        Course("Algorytmy i struktury danych", "ASD", "Ćwiczenia", "", listOf("Urbanowicz Adam"), "", "A205-206", days[i], "13:00:00", "13:50:00", "50 min", ""),
        //        Course("Java zaawansowana", "JAZ", "Ćwiczenia", "", listOf("Nenca Anna"), "", "A502", days[i], "14:00:00", "14:20:00", "20 min", ""),
        //        Course("Algebra liniowa i geometria", "ALG", "Wykład", "", listOf("Puźniakowska-Gałuch Elżbieta"), "", "A405-406", days[i], "15:00:00", "15:40:00", "40 min", ""),
        //    ))
        //}
//
        //courses = empty
    }

    var fetched = Date(0)

    fun update(value: List<Course>) {
        courses = value.sortedWith(compareBy(Course::date, Course::start, Course::end))
        fetched = Date()
        this.saveToFile()
    }

    fun later(date: LocalDateTime = LocalDateTime.now()): List<Course> =
        courses.filter {
            it.date.atTime(it.end).isAfter(date) &&
                    it.date.compareTo(date.toLocalDate()) == 0
        }

    fun after(date: LocalDateTime = LocalDateTime.now()): List<Course> =
        courses.filter { it.date.atTime(it.end).isAfter(date) }

    fun day(date: LocalDate = LocalDate.now()): List<Course> =
        courses.filter { it.date.compareTo(date) == 0 }

    fun week(date: LocalDate = LocalDate.now()): Map<LocalDate, List<Course>> {
        val start = date.minusDays(date.dayOfWeek.value.toLong())
        val end = start.plusWeeks(1)
        return courses
            .filter { it.date.isAfter(start) && it.date.isBefore(end) }
            .groupBy { it.date }
    }
}