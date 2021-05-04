package eu.alessiobianchi.log.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import eu.alessiobianchi.log.Log;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.v("Ciao", "msg");
		Log.v("Ciao", new Exception(), "msg");

		Log.v("Ciao");
		Log.v("Ciao", new Exception());

	}
}
