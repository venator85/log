@file:JvmName("Log")

package eu.alessiobianchi.log

import java.io.File
import java.util.concurrent.locks.ReentrantLock

object Log {
	@JvmStatic
	val lock = ReentrantLock()

	@JvmStatic
	var enabled = true
		@JvmName("isEnabled") get

	@JvmStatic
	var logFile: File? = null
		private set

	@JvmStatic
	fun init(enableLogcat: Boolean, logFile: File?) {
	}

	@JvmStatic
	inline fun withLock(crossinline block: () -> Unit) {
	}

	@JvmStatic
	fun v(msg: String?, t: Throwable?, tag: Any?) {
	}

	@JvmStatic
	fun d(msg: String?, t: Throwable?, tag: Any?) {
	}

	@JvmStatic
	fun i(msg: String?, t: Throwable?, tag: Any?) {
	}

	@JvmStatic
	fun w(msg: String?, t: Throwable?, tag: Any?) {
	}

	@JvmStatic
	fun e(msg: String?, t: Throwable?, tag: Any?) {
	}


	@JvmStatic
	fun v(msg: String?, tag: Any?) {
	}

	@JvmStatic
	fun d(msg: String?, tag: Any?) {
	}

	@JvmStatic
	fun i(msg: String?, tag: Any?) {
	}

	@JvmStatic
	fun w(msg: String?, tag: Any?) {
	}

	@JvmStatic
	fun e(msg: String?, tag: Any?) {
	}

}
