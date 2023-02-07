package gq97a6.pjatk.app

infix fun <E> (() -> E?).catch(e: E): E =
    try {
        this() ?: e
    } catch (_: Exception) {
        e
    }