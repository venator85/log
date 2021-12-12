package eu.alessiobianchi.log

import java.util.concurrent.locks.ReentrantLock

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

    protected open fun findMinLevel() = 0

    override fun doLog(lock: ReentrantLock, level: Int, msg: String, tag: String) {
    }
}

open class ConsoleLogger : ILogger {
    override fun doLog(lock: ReentrantLock, level: Int, msg: String, tag: String) {
    }
}
