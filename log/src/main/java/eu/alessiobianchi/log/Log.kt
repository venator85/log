@file:JvmName("Log")

package eu.alessiobianchi.log

import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.math.min

object Log {
	private const val MAXIMUM_LINE_LENGTH = 4000
	private const val MSG_FORMAT = "%s [%s][%s]:   %s\n"
	@JvmStatic
	private val TIMESTAMP_FORMAT = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)

	@JvmStatic
	val lock = ReentrantLock()

	@JvmStatic
	var enabled = true
		@JvmName("isEnabled") get

	@JvmStatic
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

	@JvmStatic
	private var writer: BufferedWriter? = null

	@JvmStatic
	fun init(enableLogcat: Boolean, logFile: File?) {
		enabled = enableLogcat
		this.logFile = logFile
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

	@JvmStatic
	inline fun withLock(crossinline block: () -> Unit) {
		lock.withLock {
			block()
		}
	}

	@JvmStatic
	fun v(msg: String?, t: Throwable?, tag: Any?) = log(android.util.Log.VERBOSE, getTag(tag), msg, t)

	@JvmStatic
	fun d(msg: String?, t: Throwable?, tag: Any?) = log(android.util.Log.DEBUG, getTag(tag), msg, t)

	@JvmStatic
	fun i(msg: String?, t: Throwable?, tag: Any?) = log(android.util.Log.INFO, getTag(tag), msg, t)

	@JvmStatic
	fun w(msg: String?, t: Throwable?, tag: Any?) = log(android.util.Log.WARN, getTag(tag), msg, t)

	@JvmStatic
	fun e(msg: String?, t: Throwable?, tag: Any?) = log(android.util.Log.ERROR, getTag(tag), msg, t)


	@JvmStatic
	fun v(msg: String?, tag: Any?) = log(android.util.Log.VERBOSE, getTag(tag), msg, null)

	@JvmStatic
	fun d(msg: String?, tag: Any?) = log(android.util.Log.DEBUG, getTag(tag), msg, null)

	@JvmStatic
	fun i(msg: String?, tag: Any?) = log(android.util.Log.INFO, getTag(tag), msg, null)

	@JvmStatic
	fun w(msg: String?, tag: Any?) = log(android.util.Log.WARN, getTag(tag), msg, null)

	@JvmStatic
	fun e(msg: String?, tag: Any?) = log(android.util.Log.ERROR, getTag(tag), msg, null)


	@JvmStatic
	private fun log(level: Int, tag: String, message: String?, t: Throwable?) {
		val msg = message ?: "null"
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

	@JvmStatic
	private fun logcat(level: Int, tag: String, msg: String) {
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
						android.util.Log.println(level, tag, part)
						i = end
					} while (i < newline)
					i++
				}
			}
		} else {
			android.util.Log.println(level, tag, msg)
		}
	}

	@JvmStatic
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

	@JvmStatic
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

	@JvmStatic
	fun getTag(obj: Any?): String {
		return if (obj != null) {
			var tag: String?
			if (obj is String) {
				tag = obj
				if (tag.isEmpty()) {
					tag = "(no-tag)"
				}
			} else {
				val c: Class<*> = if (obj is Class<*>) {
					obj
				} else {
					obj.javaClass
				}
				tag = if (c.isAnonymousClass) {
					val name = c.name
					val pos = name.lastIndexOf('.')
					if (pos == -1 || pos == name.length - 1) {
						name
					} else {
						name.substring(pos + 1)
					}
				} else {
					c.simpleName
				}
				if (tag!!.isEmpty()) {
					tag = c.name
				}
			}
			tag ?: "(null)"
		} else {
			"(null)"
		}
	}

}

