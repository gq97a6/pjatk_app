package gq97a6.pjatk.app

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

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