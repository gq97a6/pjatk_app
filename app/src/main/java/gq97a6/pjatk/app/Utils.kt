package gq97a6.pjatk.app

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.time.*
import java.time.Month.*
import java.time.temporal.ChronoUnit

infix fun <E> (() -> E?).catch(e: E): E =
    try {
        this() ?: e
    } catch (_: Exception) {
        e
    }

fun createToast(context: Context, msg: String, time: Int = 0) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        Toast.makeText(context, msg, time).show()
    } else {
        Handler(Looper.getMainLooper()).post { Toast.makeText(context, msg, time).show() }
    }
}

fun eta(to: LocalTime, from: LocalTime = LocalTime.now()): String {
    if (from.isAfter(to)) return "trwa"

    val seconds = from.until(to, ChronoUnit.SECONDS)
    if (seconds <= 60) return "${seconds}s"

    val hours = from.until(to, ChronoUnit.HOURS)
    val minutes = from.plusHours(hours).until(to, ChronoUnit.MINUTES)

    return "${hours}h ${minutes}m"
}

fun LocalDate.dzien() = when (this.dayOfWeek) {
    DayOfWeek.MONDAY -> "PONIEDZIAŁEK"
    DayOfWeek.TUESDAY -> "WTOREK"
    DayOfWeek.WEDNESDAY -> "ŚRODA"
    DayOfWeek.THURSDAY -> "CZWARTEK"
    DayOfWeek.FRIDAY -> "PIĄTEK"
    DayOfWeek.SATURDAY -> "SOBOTA"
    DayOfWeek.SUNDAY -> "NIEDZIELA"
    else -> ""
}
