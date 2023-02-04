package gq97a6.pjatk.app

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.io.FileReader
import java.sql.Time
import kotlin.reflect.KClass

object Storage {
    val mapper: ObjectMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    var rootFolder: String = ""
        set(value) {
            field = value
            path = mapOf(
                Settings::class to "$value/settings",
                Timetable::class to "$value/timetable"
            )
        }

    lateinit var path: Map<KClass<out Any>, String>

    fun Any.prepareSave(): String = mapper.writeValueAsString(this)

    fun Any.saveToFile(save: String = this.prepareSave()) {
        try {
            val path = path[this::class]
            File(path!!).writeText(save)
        } catch (e: Exception) {
            run {}
        }
    }

    inline fun <reified T> Collection<T>.saveToFile(save: String = this.prepareSave()) {
        try {
            val path = path[T::class]
            File(path!!).writeText(save)
        } catch (_: Exception) {
        }
    }

    inline fun <reified T> getSave() = try {
        FileReader(path[T::class]).readText()
    } catch (_: Exception) {
        ""
    }

    inline fun <reified T> parseSave(save: String = getSave<T>()): T? =
        try {
            mapper.readValue(save, T::class.java)
        } catch (_: Exception) {
            null
        }

    inline fun <reified T> parseListSave(save: String = getSave<T>()): MutableList<T> =
        try {
            mapper.readerForListOf(T::class.java).readValue(save)
        } catch (_: Exception) {
            mutableListOf()
        }
}