@file:JvmName("Log")

package eu.alessiobianchi.log

import java.io.File
import java.util.concurrent.locks.ReentrantLock

val lock = ReentrantLock()

var enabled = true
	@JvmName("isEnabled") get

var logFile: File? = null
	private set

fun init(enableLogcat: Boolean, logFile: File?) {
}

inline fun withLock(crossinline block: () -> Unit) {
}

@JvmOverloads
fun v(msg: String, t: Throwable? = null, tag: Any?) {
}

@JvmOverloads
fun d(msg: String, t: Throwable? = null, tag: Any?) {
}

@JvmOverloads
fun i(msg: String, t: Throwable? = null, tag: Any?) {
}

@JvmOverloads
fun w(msg: String, t: Throwable? = null, tag: Any?) {
}

@JvmOverloads
fun e(msg: String, t: Throwable? = null, tag: Any?) {
}
