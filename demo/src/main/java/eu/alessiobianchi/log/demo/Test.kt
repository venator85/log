package eu.alessiobianchi.log.demo

import eu.alessiobianchi.log.Log

fun test() {

	Log.e("Ciao", "msg")
	Log.e("Ciao", Exception(), "msg")

}