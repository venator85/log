package eu.alessiobianchi.log.demo;

import android.app.Activity;
import android.os.Bundle;

import eu.alessiobianchi.log.Log;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.e("Ciao", this);
		Log.e("Ciao", new Exception("s"), this);

	}
}
