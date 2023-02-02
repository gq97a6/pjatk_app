package gq97a6.pjatk.app

import java.util.Date
import java.sql.Time

data class Course(
    val name: String,
    val code: String,
    val type: String,
    val groups: String,
    val educators: List<String>,
    val building: String,
    val room: String,
    val date: Date,
    val start: Date,
    val end: Date,
    val len: String,
    val msCode: String
)