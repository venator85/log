package eu.alessiobianchi.log.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import eu.alessiobianchi.log.Log
import kotlinx.coroutines.delay

class KotlinActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		Log.e("Ciao", "msg")
		Log.e("Ciao", StringBuilder("msg"))
		Log.e("Ciao", Exception(), "msg")

		Log.e("NoTag1")
		Log.e("NoTag2", Exception())

		Log.withLock {
		}

		Runnable {
			Log.e("Test da anonymous inner class", this)
			Log.e("Test da anonymous inner class AutoTag")
		}.run()

		lifecycleScope.launchWhenStarted {
			delay(100)
			Log.e("Test da coroutine", this)
			Log.e("Test da coroutine AutoTag")
		}
	}

}
