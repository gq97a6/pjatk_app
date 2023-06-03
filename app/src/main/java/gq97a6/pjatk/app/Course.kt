package gq97a6.pjatk.app

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class Course(
    val name: String = "",
    val code: String = "",
    val type: String = "",
    val groups: String = "",
    val educators: List<String> = listOf(),
    val building: String = "",
    val room: String = "",
    val dateString: String = "",
    val startString: String = "",
    val endString: String = "",
    val len: String = "",
    val msCode: String = ""
) {
    var date = { LocalDate.parse(dateString, pattern("dd.MM.yyyy")) } catch LocalDate.MIN
    var start = { LocalTime.parse(startString, pattern("HH:mm:ss")) } catch LocalTime.MIN
    var end = { LocalTime.parse(endString, pattern("HH:mm:ss")) } catch LocalTime.MIN

    //TODO
    val startIn
        get() = eta(start)

    val endIn
        get() = eta(end)

    private fun pattern(of: String) =
        DateTimeFormatter.ofPattern(of, Locale.forLanguageTag("pl-PL"))
}