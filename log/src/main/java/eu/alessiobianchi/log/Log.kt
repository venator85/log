@file:JvmName("Log")

package eu.alessiobianchi.log

import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.math.min

private const val MAXIMUM_LINE_LENGTH = 4000
private val TIMESTAMP_FORMAT = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)
private const val MSG_FORMAT = "%s [%s][%s]:   %s\n"

val lock = ReentrantLock()

var enabled = true
	@JvmName("isEnabled") get

var logFile: File? = null
	private set
	get() {
		lock.withLock {
			if (writer != null) {
				try {
					writer!!.flush()
				} catch (e: IOException) {
					logcat(android.util.Log.ERROR, "Log", "Error flushing log file")
					logcat(android.util.Log.ERROR, "Log", getStackTraceString(e))
				}
			}
			return logFile
		}
	}
private var writer: BufferedWriter? = null

fun init(enableLogcat: Boolean, logFile: File?) {
	enabled = enableLogcat
	eu.alessiobianchi.log.logFile = logFile
	if (logFile != null) {
		lock.withLock {
			if (writer != null) {
				try {
					writer!!.close()
				} catch (ignore: IOException) {
				}
			}
			writer = try {
				BufferedWriter(FileWriter(logFile, false))
			} catch (e: IOException) {
				throw RuntimeException(e.message, e)
			}
		}
	}
}

inline fun withLock(crossinline block: () -> Unit) {
	lock.withLock {
		block()
	}
}

@JvmOverloads
fun v(msg: String, t: Throwable? = null, tag: Any?) = log(android.util.Log.VERBOSE, LogUtils.getTag(tag), msg, t)

@JvmOverloads
fun d(msg: String, t: Throwable? = null, tag: Any?) = log(android.util.Log.DEBUG, LogUtils.getTag(tag), msg, t)

@JvmOverloads
fun i(msg: String, t: Throwable? = null, tag: Any?) = log(android.util.Log.INFO, LogUtils.getTag(tag), msg, t)

@JvmOverloads
fun w(msg: String, t: Throwable? = null, tag: Any?) = log(android.util.Log.WARN, LogUtils.getTag(tag), msg, t)

@JvmOverloads
fun e(msg: String, t: Throwable? = null, tag: Any?) = log(android.util.Log.ERROR, LogUtils.getTag(tag), msg, t)

private fun log(level: Int, tag: String, msg: String, t: Throwable?) {
	val stacktrace = if (t != null) getStackTraceString(t) else null
	if (stacktrace == null) {
		// fast path without single locking
		logcat(level, tag, msg)
	} else {
		lock.withLock {
			logcat(level, tag, msg)
			logcat(level, tag, stacktrace)
		}
	}
	if (writer != null) {
		lock.withLock {
			logToFile(level, tag, msg)
			if (stacktrace != null) logToFile(level, tag, stacktrace)
		}
	}
}

private fun logcat(level: Int, tag: String, msg: String) {
	val msgLen = msg.length
	if (msgLen > MAXIMUM_LINE_LENGTH) {
		var i = 0
		while (i < msgLen) {
			var newline = msg.indexOf('\n', i)
			newline = if (newline != -1) newline else msgLen
			do {
				val end = min(newline, i + MAXIMUM_LINE_LENGTH)
				val part = msg.substring(i, end)
				android.util.Log.println(level, tag, part)
				i = end
			} while (i < newline)
			i++
		}
	} else {
		android.util.Log.println(level, tag, msg)
	}
}

private fun logToFile(level: Int, tag: String, msg: String) {
	try {
		val sLevel: String = when (level) {
			android.util.Log.VERBOSE -> "V"
			android.util.Log.DEBUG -> "D"
			android.util.Log.INFO -> "I"
			android.util.Log.WARN -> "W"
			android.util.Log.ERROR -> "E"
			else -> level.toString()
		}
		writer!!.write(String.format(Locale.US, MSG_FORMAT, TIMESTAMP_FORMAT.format(Date()), sLevel, tag, msg))
	} catch (e: IOException) {
		logcat(android.util.Log.ERROR, "Log", "Error writing log to file")
		logcat(android.util.Log.ERROR, "Log", getStackTraceString(e))
	}
}

private fun getStackTraceString(tr: Throwable?): String {
	if (tr == null) {
		return ""
	}
	val sw = StringWriter(256)
	val pw = PrintWriter(sw, false)
	tr.printStackTrace(pw)
	pw.flush()
	return sw.toString()
}
