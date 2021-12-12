package eu.alessiobianchi.log.demo

import eu.alessiobianchi.log.ConsoleLogger
import eu.alessiobianchi.log.Log
import org.junit.Before
import org.junit.Test

class Test {
    @Before
    fun setup() {
        Log.impl = ConsoleLogger()
    }

    @Test
    fun test1() {
        Log.e("Hello world!")
        Log.e("Hello world 2!", Exception("TEST"))
    }
}
