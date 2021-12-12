package eu.alessiobianchi.log

import android.os.Build
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.math.min

interface ILogger {
    fun doLog(lock: ReentrantLock, level: Int, msg: String, tag: String)
}

open class NoOpLogger : ILogger {
    override fun doLog(lock: ReentrantLock, level: Int, msg: String, tag: String) {
    }
}

open class AndroidLogcat : ILogger {
    companion object {
        protected const val MAXIMUM_LINE_LENGTH = 4000
    }

    private val minLevel by lazy { findMinLevel() }

    protected open fun findMinLevel() = when (Build.MANUFACTURER.lowercase(Locale.US)) {
        "sony" -> android.util.Log.INFO
        "zte" -> android.util.Log.DEBUG
        else -> 0
    }

    override fun doLog(lock: ReentrantLock, level: Int, msg: String, tag: String) {
        val adjLevel = level.coerceAtLeast(minLevel)

        val msgLen = msg.length
        if (msgLen > MAXIMUM_LINE_LENGTH) {
            lock.withLock {
                var i = 0
                while (i < msgLen) {
                    var newline = msg.indexOf('\n', i)
                    newline = if (newline != -1) newline else msgLen
                    do {
                        val end = min(newline, i + MAXIMUM_LINE_LENGTH)
                        val part = msg.substring(i, end)
                        android.util.Log.println(adjLevel, tag, part)
                        i = end
                    } while (i < newline)
                    i++
                }
            }
        } else {
            android.util.Log.println(adjLevel, tag, msg)
        }
    }
}

open class ConsoleLogger : ILogger {
    override fun doLog(lock: ReentrantLock, level: Int, msg: String, tag: String) {
        val sLevel = Log.logLevelToString(level)
        println("$sLevel/$tag: $msg")
    }
}
